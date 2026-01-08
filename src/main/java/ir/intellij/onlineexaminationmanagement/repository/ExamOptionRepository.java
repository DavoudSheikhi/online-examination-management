package ir.intellij.onlineexaminationmanagement.repository;

import ir.intellij.onlineexaminationmanagement.model.ExamOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamOptionRepository extends JpaRepository<ExamOption, Long> {
}
