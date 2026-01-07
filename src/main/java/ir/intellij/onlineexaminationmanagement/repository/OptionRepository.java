package ir.intellij.onlineexaminationmanagement.repository;

import ir.intellij.onlineexaminationmanagement.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {
}
