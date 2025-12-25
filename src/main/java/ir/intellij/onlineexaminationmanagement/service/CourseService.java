package ir.intellij.onlineexaminationmanagement.service;

import ir.intellij.onlineexaminationmanagement.dto.CourseRequestDto;
import ir.intellij.onlineexaminationmanagement.dto.CourseResponseDto;
import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.model.User;

import java.util.List;

public interface CourseService {
    List<Course> findAll();

    Course findByCourseCode(String courseCode);

    List<CourseResponseDto> entityToResponseList(List<Course> courseList);

    Course saveCourseFromDto(CourseRequestDto dto);

    CourseResponseDto entityToDto(Course course);

    void deleteCourseByCourseCode(String courseCode);

    void updateCourse(String courseCode, CourseRequestDto dto);

    void assignTeacher(String courseCode, String username);

    void deleteTeacherFromCourse(Course byCourseCode);

    void addStudent(Course course, User user);

    void removeStudent(Course course, User user);

    List<Course> findCoursesByStudent(User student);

    List<Course> findCoursesByTeacher(User teacher);
}
