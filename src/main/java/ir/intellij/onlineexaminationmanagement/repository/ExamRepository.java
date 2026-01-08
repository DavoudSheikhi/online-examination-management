package ir.intellij.onlineexaminationmanagement.repository;

import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.model.Exam;
import ir.intellij.onlineexaminationmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByCourse(Course course);

    List<Exam> findByCourseAndCreatedBy(Course course, User createdBy);

    List<Exam> findByCourse_CourseCodeAndCreatedBy_Username(String courseCode, String username);

    Exam findByExamCode(String examCode);

    @Query("""
                select distinct e
                from Exam e
                join e.course c
                join c.enrolledStudents s
                where c.courseCode = :courseCode
                  and s.username = :username
            """)
    List<Exam> findAvailableExamsForStudent(
            @Param("courseCode") String courseCode,
            @Param("username") String username
    );

}
