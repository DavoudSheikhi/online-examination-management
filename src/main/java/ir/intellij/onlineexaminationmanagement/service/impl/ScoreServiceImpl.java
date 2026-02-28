package ir.intellij.onlineexaminationmanagement.service.impl;

import ir.intellij.onlineexaminationmanagement.model.*;
import ir.intellij.onlineexaminationmanagement.repository.AttemptAnswerRepository;
import ir.intellij.onlineexaminationmanagement.repository.ExamAttemptRepository;
import ir.intellij.onlineexaminationmanagement.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final AttemptAnswerRepository attemptAnswerRepository;
    private final ExamAttemptRepository examAttemptRepository;

    @Override
    @Transactional
    public void finalizeAutoScoring(ExamAttempt attempt) {

        List<AttemptAnswer> answers = attemptAnswerRepository.findAllByAttemptIdWithQuestion(attempt.getId());

        double autoTotal = 0d;
        double manualTotal = 0d;

        for (AttemptAnswer aa : answers) {
            ExamQuestion q = aa.getExamQuestion();
            double auto = 0d;

            if (q.getQuestionType() == QuestionType.MULTIPLE_CHOICE && q instanceof ExamMultipleChoiceQuestion mcq) {
                ExamOption correct = mcq.getCorrectExamOption();
                ExamOption selected = aa.getSelectedExamOption();
                if (correct != null && selected != null && correct.getId() != null && correct.getId().equals(selected.getId())) {
                    auto = q.getScore() == null ? 0d : q.getScore();
                }
            }

            aa.setAutoScore(auto);
            aa.setFinalScore(auto + (aa.getManualScore() == null ? 0d : aa.getManualScore()));
            attemptAnswerRepository.save(aa);

            autoTotal += aa.getAutoScore() == null ? 0d : aa.getAutoScore();
            manualTotal += aa.getManualScore() == null ? 0d : aa.getManualScore();
        }

        attempt.setTotalAutoScore(autoTotal);
        attempt.setTotalManualScore(manualTotal);
        attempt.setTotalScore(autoTotal + manualTotal);
        examAttemptRepository.save(attempt);
    }

    @Override
    @Transactional
    public void setManualScore(Long attemptAnswerId, double manualScore, String teacherUsername) {
        AttemptAnswer aa = attemptAnswerRepository.findById(attemptAnswerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found: " + attemptAnswerId));

        ExamAttempt attempt = aa.getAttempt();
        if (!attempt.getExam().getCreatedBy().getUsername().equals(teacherUsername)) {
            throw new IllegalStateException("Access denied");
        }

        ExamQuestion q = aa.getExamQuestion();
        double max = (q == null || q.getScore() == null) ? 0d : q.getScore();
        if (manualScore < 0 || manualScore > max) {
            throw new IllegalArgumentException("Manual score must be between 0 and " + max);
        }

        aa.setManualScore(manualScore);
        aa.setFinalScore((aa.getAutoScore() == null ? 0d : aa.getAutoScore()) + manualScore);
        attemptAnswerRepository.save(aa);

        List<AttemptAnswer> answers = attemptAnswerRepository.findAllByAttemptIdWithQuestion(attempt.getId());
        double autoTotal = 0d;
        double manualTotal = 0d;
        for (AttemptAnswer a : answers) {
            autoTotal += a.getAutoScore() == null ? 0d : a.getAutoScore();
            manualTotal += a.getManualScore() == null ? 0d : a.getManualScore();
        }
        attempt.setTotalAutoScore(autoTotal);
        attempt.setTotalManualScore(manualTotal);
        attempt.setTotalScore(autoTotal + manualTotal);
        examAttemptRepository.save(attempt);
    }
}
