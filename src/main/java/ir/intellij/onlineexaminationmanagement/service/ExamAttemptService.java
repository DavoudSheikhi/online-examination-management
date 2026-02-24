package ir.intellij.onlineexaminationmanagement.service;

import ir.intellij.onlineexaminationmanagement.model.AttemptAnswer;
import ir.intellij.onlineexaminationmanagement.model.ExamAttempt;

import java.util.List;

public interface ExamAttemptService {

    ExamAttempt startOrResumeAttempt(String examCode, String studentUsername);

    long getRemainingSeconds(Long attemptId, String studentUsername);

    ExamAttempt findAttemptForStudent(Long attemptId, String studentUsername);

    List<AttemptAnswer> getAttemptAnswers(Long attemptId, String studentUsername);

    void autosaveAnswer(Long attemptId,
                        String studentUsername,
                        Long examQuestionId,
                        Long selectedExamOptionId,
                        String descriptiveText);

    ExamAttempt submitAttempt(Long attemptId, String studentUsername);
}
