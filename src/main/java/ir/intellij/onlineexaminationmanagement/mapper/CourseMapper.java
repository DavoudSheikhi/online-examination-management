package ir.intellij.onlineexaminationmanagement.mapper;

import ir.intellij.onlineexaminationmanagement.dto.CourseRequestDto;
import ir.intellij.onlineexaminationmanagement.dto.CourseResponseDto;
import ir.intellij.onlineexaminationmanagement.model.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseMapper {

    public static Course courseDtoToEntity(CourseRequestDto dto) {
        return Course.builder()
                .title(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }

    public static CourseResponseDto entityToCourseDto(Course course) {

        return new CourseResponseDto(
                course.getTitle(),
                course.getCourseCode(),
                course.getStartDate(),
                course.getEndDate());
    }

    public static List<CourseResponseDto> entityListToResponseList(List<Course> courses) {
        List<CourseResponseDto> courseResponseList = new ArrayList<>();
        for (Course course : courses) {
            courseResponseList.add(entityToCourseDto(course));
        }
        return courseResponseList;
    }
}
