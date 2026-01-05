package ir.intellij.onlineexaminationmanagement.service;

import ir.intellij.onlineexaminationmanagement.model.ExamQuestion;

import java.util.List;

public interface ExamQuestionService {
    List<ExamQuestion> findExamQuestions(String examCode);

    ExamQuestion save(ExamQuestion examQuestion);
}
