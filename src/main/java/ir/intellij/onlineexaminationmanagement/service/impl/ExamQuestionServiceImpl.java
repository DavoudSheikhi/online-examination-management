package ir.intellij.onlineexaminationmanagement.service.impl;

import ir.intellij.onlineexaminationmanagement.model.Exam;
import ir.intellij.onlineexaminationmanagement.model.ExamQuestion;
import ir.intellij.onlineexaminationmanagement.repository.ExamQuestionRepository;
import ir.intellij.onlineexaminationmanagement.repository.ExamRepository;
import ir.intellij.onlineexaminationmanagement.service.ExamQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamQuestionServiceImpl implements ExamQuestionService {
    private final ExamRepository examRepository;
    private final ExamQuestionRepository examQuestionRepository;

    @Override
    public List<ExamQuestion> findExamQuestions(String examCode) {
        Exam byExamCode = examRepository.findByExamCode(examCode);
        return examQuestionRepository.findByExam(byExamCode);
    }

    @Override
    public ExamQuestion save(ExamQuestion examQuestion) {
        return examQuestionRepository.save(examQuestion);
    }
}
