package ir.intellij.onlineexaminationmanagement.service;

import ir.intellij.onlineexaminationmanagement.dto.UserRequestDto;
import ir.intellij.onlineexaminationmanagement.dto.UserResponseDto;
import ir.intellij.onlineexaminationmanagement.model.Role;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.model.UserStatus;

import java.util.List;

public interface UserService {

    User save(User user);

    User findByUsername(String username);

    UserResponseDto entityToResponseDto(User user);

    UserResponseDto saveEntityFromDto(UserRequestDto userRequestDto);

    List<User> findUsersByUserStatus(UserStatus userStatus);

    List<UserResponseDto> entityListToResponseList(List<User> users);

    List<User> findAll();

    void delete(User user);

    List<User> search(String fullName,
                      String username,
                      String phone,
                      Integer age,
                      Role role,
                      UserStatus status);

    List<User> findAllApprovedTeachers();

    List<User> findEligibleStudents(String courseCode, Role role, UserStatus status);


    void changeRole(String username, Role role);

    void changeStatus(String username, String newStatus);

    void deleteUserFromEnrolledCourses(User user);
}


