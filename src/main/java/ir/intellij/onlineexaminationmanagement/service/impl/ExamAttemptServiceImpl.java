package ir.intellij.onlineexaminationmanagement.service.impl;

import ir.intellij.onlineexaminationmanagement.model.*;
import ir.intellij.onlineexaminationmanagement.repository.AttemptAnswerRepository;
import ir.intellij.onlineexaminationmanagement.repository.ExamAttemptRepository;
import ir.intellij.onlineexaminationmanagement.repository.ExamRepository;
import ir.intellij.onlineexaminationmanagement.repository.UserRepository;
import ir.intellij.onlineexaminationmanagement.service.ExamAttemptService;
import ir.intellij.onlineexaminationmanagement.service.ExamQuestionService;
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
        return attempt;
    }





}
