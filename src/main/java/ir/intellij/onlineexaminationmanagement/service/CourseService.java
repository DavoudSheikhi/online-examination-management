package ir.intellij.onlineexaminationmanagement.service;

import ir.intellij.onlineexaminationmanagement.dto.CourseRequestDto;
import ir.intellij.onlineexaminationmanagement.dto.CourseResponseDto;
import ir.intellij.onlineexaminationmanagement.model.Course;

import java.util.List;

public interface CourseService {
    List<Course> findAll();

    Course findByCourseCode(String courseCode);

    List<CourseResponseDto> entityToResponseList(List<Course> courseList);

    Course saveCourseFromDto(CourseRequestDto dto);

    CourseResponseDto entityToDto(Course course);

    void deleteCourseByCourseCode(String courseCode);

    void updateCourse(String courseCode, CourseRequestDto dto);
}
