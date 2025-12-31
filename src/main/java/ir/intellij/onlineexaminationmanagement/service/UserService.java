package ir.intellij.onlineexaminationmanagement.service;

import ir.intellij.onlineexaminationmanagement.dto.user.UserRegisterDTO;
import ir.intellij.onlineexaminationmanagement.dto.user.UserResponseDto;
import ir.intellij.onlineexaminationmanagement.dto.user.UserUpdateInfoDTO;
import ir.intellij.onlineexaminationmanagement.model.Role;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.model.UserStatus;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {

    User save(User user);

    User findByUsername(String username);

    UserResponseDto entityToResponseDto(User user);

    User register(UserRegisterDTO registerDTO);

//    UserResponseDto saveEntity(User user);

    List<User> findUsersByUserStatus(UserStatus userStatus);

    List<UserResponseDto> entityListToResponseList(List<User> users);

    void updateUserInfo( UserUpdateInfoDTO dto, User user);

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

    List<User> findAllUsersInCourse(String courseCode);



}


