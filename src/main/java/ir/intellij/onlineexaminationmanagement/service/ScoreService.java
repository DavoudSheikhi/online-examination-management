package ir.intellij.onlineexaminationmanagement.service;

import ir.intellij.onlineexaminationmanagement.model.ExamAttempt;

public interface ScoreService {

    void finalizeAutoScoring(ExamAttempt attempt);

    void setManualScore(Long attemptAnswerId, double manualScore, String teacherUsername);
}

