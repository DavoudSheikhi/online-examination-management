package ir.intellij.onlineexaminationmanagement.service.impl;

import ir.intellij.onlineexaminationmanagement.dto.user.UserRegisterDTO;
import ir.intellij.onlineexaminationmanagement.dto.user.UserResponseDto;
import ir.intellij.onlineexaminationmanagement.dto.user.UserUpdateInfoDTO;
import ir.intellij.onlineexaminationmanagement.mapper.UserMapper;
import ir.intellij.onlineexaminationmanagement.model.*;
import ir.intellij.onlineexaminationmanagement.repository.CourseRepository;
import ir.intellij.onlineexaminationmanagement.repository.UserRepository;
import ir.intellij.onlineexaminationmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserResponseDto entityToResponseDto(User user) {
        return userMapper.entityToResponse(user);
    }

    @Override
    public User register(UserRegisterDTO registerDTO) {
        User user;
        if (registerDTO.role().equals("STUDENT")) {
            Student student = new Student();
            userMapper.updateUser(registerDTO, student);
            student.setPassword(passwordEncoder.encode(registerDTO.password()));
            student.setUserStatus(UserStatus.PENDING);
            student.setActive(true);
            user = student;
        } else if (registerDTO.role().equals("TEACHER")) {
            Teacher teacher = new Teacher();
            userMapper.updateUser(registerDTO, teacher);
            teacher.setPassword(passwordEncoder.encode(registerDTO.password()));
            teacher.setUserStatus(UserStatus.PENDING);
            teacher.setActive(true);
            user = teacher;
        } else {
            throw new IllegalArgumentException("Role نامعتبر است: " + registerDTO.role());
        }
        return userRepository.save(user);
    }


//    @Override
//    public UserResponseDto saveEntity(User user) {
//
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        User savedUser = userRepository.save(user);
//        return UserMapper.entityToResponse(savedUser);
//    }

    @Override
    public List<User> findUsersByUserStatus(UserStatus userStatus) {
        return userRepository.findUsersByUserStatus(userStatus);
    }

    @Override
    public List<UserResponseDto> entityListToResponseList(List<User> users) {
        return userMapper.entityListToResponseList(users);
    }

    @Override
    public void updateUserInfo(UserUpdateInfoDTO dto, User user) {
        userMapper.updateUserInfo(dto, user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(User user) {
        List<Course> coursesByStudent = courseRepository.findCoursesByStudent(user);
        for (Course course : coursesByStudent) {
            course.getEnrolledStudents().remove(user);
        }
        userRepository.delete(user);
    }

    @Override
    public List<User> search(String fullName, String username, String phone, Integer age, Role role, UserStatus status) {
        return userRepository.searchUsers(fullName, username, phone, age, role, status);
    }

    @Override
    public List<User> findAllApprovedTeachers() {
        return userRepository.findUsersByRoleAndUserStatus(Role.TEACHER, UserStatus.APPROVED);
    }

    @Override
    public List<User> findEligibleStudents(String courseCode, Role role, UserStatus status) {
        return userRepository.findEligibleStudents(courseCode, role, status);
    }

    @Override
    @Transactional
    public void changeRole(String username, Role role) {
        User byUsername = userRepository.findByUsername(username);
        deleteUserFromEnrolledCourses(byUsername);
        byUsername.setRole(role);
        userRepository.save(byUsername);
    }

    @Override
    @Transactional
    public void changeStatus(String username, String newStatus) {
        User byUsername = userRepository.findByUsername(username);
        if (byUsername.getUserStatus() == UserStatus.APPROVED) {
            deleteUserFromEnrolledCourses(byUsername);
        }
        byUsername.setUserStatus(UserStatus.valueOf(newStatus));
        userRepository.save(byUsername);
    }

    @Override
    public void deleteUserFromEnrolledCourses(User byUsername) {
        if (byUsername.getRole() == Role.STUDENT) {
            List<Course> coursesByStudent = courseRepository.findCoursesByStudent(byUsername);
            for (Course course : coursesByStudent) {
                course.getEnrolledStudents().remove(byUsername);
                courseRepository.save(course);
            }
        } else if (byUsername.getRole() == Role.TEACHER) {
            List<Course> coursesByTeacher = courseRepository.findCoursesByTeacher(byUsername);
            for (Course course : coursesByTeacher) {
                course.setTeacher(null);
                courseRepository.save(course);
            }
        }
    }

    @Override
    public List<User> findAllUsersInCourse(String courseCode) {
        List<User> users = new ArrayList<>();

        List<User> students = userRepository.findStudentsByCourseCode(courseCode);
        users.addAll(students);

        User teacher = userRepository.findTeacherByCourseCode(courseCode);
        if (teacher != null) {
            users.add(teacher);
        }
        return users;
    }
}
