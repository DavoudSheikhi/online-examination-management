package ir.intellij.onlineexaminationmanagement.service.impl;

import ir.intellij.onlineexaminationmanagement.dto.UserRequestDto;
import ir.intellij.onlineexaminationmanagement.dto.UserResponseDto;
import ir.intellij.onlineexaminationmanagement.mapper.UserMapper;
import ir.intellij.onlineexaminationmanagement.model.Role;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.model.UserStatus;
import ir.intellij.onlineexaminationmanagement.repository.UserRepository;
import ir.intellij.onlineexaminationmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
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
        return UserMapper.entityToResponse(user);
    }

    @Override
    public UserResponseDto saveEntityFromDto(UserRequestDto userRequestDto) {
        User user = UserMapper.reqDtoToEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return UserMapper.entityToResponse(savedUser);
    }

    @Override
    public List<User> findUsersByUserStatus(UserStatus userStatus) {
        return userRepository.findUsersByUserStatus(userStatus);
    }

    @Override
    public List<UserResponseDto> entityListToResponseList(List<User> users) {
        return UserMapper.entityListToResponseList(users);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(User user) {
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
}
