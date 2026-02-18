package ir.intellij.onlineexaminationmanagement.service;

import ir.intellij.onlineexaminationmanagement.model.ExamAttempt;

public interface ExamAttemptService {

    ExamAttempt startOrResumeAttempt(String examCode, String studentUsername);
}
