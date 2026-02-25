package ir.intellij.onlineexaminationmanagement.repository;

import ir.intellij.onlineexaminationmanagement.model.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {

    Optional<ExamAttempt> findByExam_ExamCodeAndStudent_Username(String examCode, String studentUsername);

    @Query("""
            select ea
            from ExamAttempt ea
            where ea.exam.examCode = :examCode
              and ea.exam.course.courseCode = :courseCode
              and ea.exam.createdBy.username = :teacherUsername
            """)
    List<ExamAttempt> findAttemptsForTeacherExam(
            @Param("courseCode") String courseCode,
            @Param("examCode") String examCode,
            @Param("teacherUsername") String teacherUsername
    );
}
