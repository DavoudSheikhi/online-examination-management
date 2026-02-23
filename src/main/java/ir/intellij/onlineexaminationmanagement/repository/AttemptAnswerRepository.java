package ir.intellij.onlineexaminationmanagement.repository;

import ir.intellij.onlineexaminationmanagement.model.AttemptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttemptAnswerRepository extends JpaRepository<AttemptAnswer, Long> {

    @Query("""
            select aa
            from AttemptAnswer aa
            join fetch aa.examQuestion q
            left join fetch aa.selectedExamOption seo
            where aa.attempt.id = :attemptId
            order by q.id asc
            """)
    List<AttemptAnswer> findAllByAttemptIdWithQuestion(@Param("attemptId") Long attemptId);

    Optional<AttemptAnswer> findByAttempt_IdAndExamQuestion_Id(Long attemptId, Long examQuestionId);
}
