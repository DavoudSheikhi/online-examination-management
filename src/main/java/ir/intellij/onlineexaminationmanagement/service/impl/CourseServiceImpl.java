package ir.intellij.onlineexaminationmanagement.service.impl;

import ir.intellij.onlineexaminationmanagement.dto.CourseRequestDto;
import ir.intellij.onlineexaminationmanagement.dto.CourseResponseDto;
import ir.intellij.onlineexaminationmanagement.mapper.CourseMapper;
import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.model.UserStatus;
import ir.intellij.onlineexaminationmanagement.repository.CourseRepository;
import ir.intellij.onlineexaminationmanagement.repository.UserRepository;
import ir.intellij.onlineexaminationmanagement.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public static String generateCourseCode(String title) {
//        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String random = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
        return title + "-" + random;
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course findByCourseCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode);
    }

    @Override
    public List<CourseResponseDto> entityToResponseList(List<Course> courseList) {
        return CourseMapper.entityListToResponseList(courseList);
    }

    @Override
    public Course saveCourseFromDto(CourseRequestDto dto) {
        Course course = CourseMapper.courseDtoToEntity(dto);
        course.setCourseCode(generateCourseCode(dto.getTitle()));
        return courseRepository.save(course);
    }

    @Override
    public CourseResponseDto entityToDto(Course course) {
        return CourseMapper.entityToCourseDto(course);
    }

    @Override
    public void deleteCourseByCourseCode(String courseCode) {
        Course byCourseCode = courseRepository.findByCourseCode(courseCode);
        if (byCourseCode != null) {
            courseRepository.delete(byCourseCode);
        }
    }

    @Override
    public void updateCourse(String courseCode, CourseRequestDto dto) {
        Course byCourseCode = courseRepository.findByCourseCode(courseCode);
        if (byCourseCode != null) {
            byCourseCode.setTitle(dto.getTitle());
            byCourseCode.setStartDate(dto.getStartDate());
            byCourseCode.setEndDate(dto.getEndDate());
            courseRepository.save(byCourseCode);
        }
    }

    @Override
    public void assignTeacher(String courseCode, String username) {
        Course byCourseCode = courseRepository.findByCourseCode(courseCode);
        User byUsername = userRepository.findByUsername(username);
        byCourseCode.setTeacher(byUsername);
        courseRepository.save(byCourseCode);
    }

    @Override
    public void deleteTeacherFromCourse(Course course) {
        course.setTeacher(null);
        courseRepository.save(course);
    }

    @Override
    public void addStudent(Course course, User user) {
        if (user.getUserStatus() != UserStatus.APPROVED) {
            throw new IllegalArgumentException("Only approved users can enroll");
        }
        course.getEnrolledStudents().add(user);
        courseRepository.save(course);
    }

    @Override
    public void removeStudent(Course course, User user) {
        course.getEnrolledStudents().remove(user);
        courseRepository.save(course);
    }

    @Override
    public List<Course> findCoursesByStudent(User user) {
        return courseRepository.findCoursesByStudent(user);
    }
}
