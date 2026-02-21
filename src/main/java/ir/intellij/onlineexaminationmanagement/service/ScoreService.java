package ir.intellij.onlineexaminationmanagement.service;

import ir.intellij.onlineexaminationmanagement.model.ExamAttempt;

public interface ScoreService {

    void finalizeAutoScoring(ExamAttempt attempt);

}

