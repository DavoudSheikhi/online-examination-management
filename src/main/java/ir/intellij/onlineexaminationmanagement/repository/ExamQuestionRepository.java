package ir.intellij.onlineexaminationmanagement.repository;

import ir.intellij.onlineexaminationmanagement.model.Exam;
import ir.intellij.onlineexaminationmanagement.model.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {
    List<ExamQuestion> findByExam(Exam exam);
}
