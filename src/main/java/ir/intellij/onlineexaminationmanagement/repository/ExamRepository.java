package ir.intellij.onlineexaminationmanagement.repository;

import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.model.Exam;
import ir.intellij.onlineexaminationmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByCourse(Course course);

    List<Exam> findByCourseAndCreatedBy(Course course, User createdBy);

    List<Exam> findByCourse_CourseCodeAndCreatedBy_Username(String courseCode, String username);
}
