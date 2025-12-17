package ir.intellij.onlineexaminationmanagement.service;

import ir.intellij.onlineexaminationmanagement.dto.UserRequestDto;
import ir.intellij.onlineexaminationmanagement.dto.UserResponseDto;
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
}
