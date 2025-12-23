package ir.intellij.onlineexaminationmanagement.repository;

import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByCourseCode(String courseCode);

    @Query("""
            select c
            from Course c
            where :user member of c.enrolledStudents
            """)
    List<Course> findCoursesByStudent(User user);

    List<Course> findCoursesByTeacher(User user);


}
