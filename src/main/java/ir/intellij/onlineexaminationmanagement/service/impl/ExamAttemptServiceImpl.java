package ir.intellij.onlineexaminationmanagement.service.impl;

import ir.intellij.onlineexaminationmanagement.model.*;
import ir.intellij.onlineexaminationmanagement.repository.*;
import ir.intellij.onlineexaminationmanagement.service.ExamAttemptService;
import ir.intellij.onlineexaminationmanagement.service.ExamQuestionService;
import ir.intellij.onlineexaminationmanagement.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamAttemptServiceImpl implements ExamAttemptService {
    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final ExamQuestionService examQuestionService;
    private final AttemptAnswerRepository attemptAnswerRepository;
    private final ScoreService scoreService;
    private final ExamOptionRepository examOptionRepository;


    @Override
    @Transactional
    public ExamAttempt startOrResumeAttempt(String examCode, String studentUsername) {
        Exam exam = examRepository.findByExamCode(examCode);
        User student = userRepository.findByUsername(studentUsername);

        ExamAttempt attempt = examAttemptRepository
                .findByExam_ExamCodeAndStudent_Username(examCode, studentUsername)
                .orElseGet(() -> {

                    Instant now = Instant.now();
                    Instant endsAt = now.plus(Duration.ofMinutes(exam.getDurationInMinutes()));

                    ExamAttempt newAttempt = ExamAttempt.builder()
                            .exam(exam)
                            .student(student)
                            .startedAt(now)
                            .endsAt(endsAt)
                            .status(ExamAttemptStatus.IN_PROGRESS)
                            .totalAutoScore(0d)
                            .totalManualScore(0d)
                            .totalScore(0d)
                            .build();

                    ExamAttempt savedAttempt = examAttemptRepository.save(newAttempt);

                    List<ExamQuestion> examQuestions = examQuestionService.findExamQuestions(examCode);

                    List<AttemptAnswer> answers = examQuestions.stream()
                            .map(q -> (AttemptAnswer) AttemptAnswer.builder()
                                    .attempt(savedAttempt)
                                    .examQuestion(q)
                                    .autoScore(0d)
                                    .manualScore(0d)
                                    .finalScore(0d)
                                    .build())
                            .toList();

                    attemptAnswerRepository.saveAll(answers);

                    return savedAttempt;
                });


        Instant now = Instant.now();
        expireIfNeededAndFinalize(attempt, now);
        if (attempt.getStatus() == ExamAttemptStatus.IN_PROGRESS) {
            return examAttemptRepository.save(attempt);
        }
        throw new IllegalStateException("بعد از تمام شدن زمان آزمون یا ثبت نهایی، امکان آزمون مجدد وجود ندارد");
    }

    @Override
    @Transactional
    public long getRemainingSeconds(Long attemptId, String studentUsername) {
        ExamAttempt attempt = findAttemptForStudent(attemptId, studentUsername);
        Instant now = Instant.now();
        expireIfNeededAndFinalize(attempt, now);
        examAttemptRepository.save(attempt);
        return Math.max(0, Duration.between(now, attempt.getEndsAt()).getSeconds());
    }

    @Override
    public ExamAttempt findAttemptForStudent(Long attemptId, String studentUsername) {
        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found: " + attemptId));

        if (attempt.getStudent() == null || attempt.getStudent().getUsername() == null
                || !attempt.getStudent().getUsername().equals(studentUsername)) {
            throw new IllegalStateException("Access denied");
        }
        return attempt;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttemptAnswer> getAttemptAnswers(Long attemptId, String studentUsername) {
        findAttemptForStudent(attemptId, studentUsername);
        return attemptAnswerRepository.findAllByAttemptIdWithQuestion(attemptId);
    }

    @Override
    @Transactional
    public void autosaveAnswer(Long attemptId,
                               String studentUsername,
                               Long examQuestionId,
                               Long selectedExamOptionId,
                               String descriptiveText) {
        ExamAttempt attempt = findAttemptForStudent(attemptId, studentUsername);
        Instant now = Instant.now();
        expireIfNeededAndFinalize(attempt, now);
        if (attempt.getStatus() != ExamAttemptStatus.IN_PROGRESS) {
            throw new IllegalStateException("Attempt is not in progress");
        }
        if (!now.isBefore(attempt.getEndsAt())) {
            throw new IllegalStateException("Time is over");
        }

        AttemptAnswer answer = attemptAnswerRepository.findByAttempt_IdAndExamQuestion_Id(attemptId, examQuestionId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found"));

        if (selectedExamOptionId != null) {
            ExamOption selected = examOptionRepository.findById(selectedExamOptionId)
                    .orElseThrow(() -> new IllegalArgumentException("Selected option not found"));
            answer.setSelectedExamOption(selected);
            answer.setDescriptiveText(null);
        } else if (descriptiveText != null) {
            answer.setDescriptiveText(descriptiveText);
            answer.setSelectedExamOption(null);
        }

//        answer.setAnsweredAt(answer.getAnsweredAt() == null ? now : answer.getAnsweredAt());
//        answer.setLastUpdatedAt(now);
        attemptAnswerRepository.save(answer);
        examAttemptRepository.save(attempt);
    }

    private void expireIfNeededAndFinalize(ExamAttempt attempt, Instant now) {
        if (attempt.getStatus() == ExamAttemptStatus.IN_PROGRESS && !now.isBefore(attempt.getEndsAt())) {
            attempt.setStatus(ExamAttemptStatus.TIME_EXPIRED);
            attempt.setSubmittedAt(now);
            ExamAttempt saved = examAttemptRepository.save(attempt);
            scoreService.finalizeAutoScoring(saved);
        }
    }
}