package ir.intellij.onlineexaminationmanagement.mapper;

import ir.intellij.onlineexaminationmanagement.dto.UserRequestDto;
import ir.intellij.onlineexaminationmanagement.dto.UserResponseDto;
import ir.intellij.onlineexaminationmanagement.model.Role;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.model.UserStatus;

import java.util.ArrayList;
import java.util.List;


public class UserMapper {

    public static User reqDtoToEntity(UserRequestDto dto) {
        return User.builder()
                .fullName(dto.getFullName())
                .phoneNumber(dto.getPhoneNumber())
                .age(dto.getAge())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .role(Role.valueOf(dto.getRole()))
                .userStatus(UserStatus.PENDING)
                .isActive(true)
                .build();
    }

    public static UserResponseDto entityToResponse(User user) {

        return new UserResponseDto(
                user.getFullName(),
                user.getPhoneNumber(),
                user.getAge(),
                user.getUsername(),
                String.valueOf(user.getRole()),
                user.getUserStatus().toString()
        );
    }

    public static List<UserResponseDto> entityListToResponseList(List<User> users) {
        List<UserResponseDto> userResponseList = new ArrayList<>();
        for (User user : users) {
            userResponseList.add(entityToResponse(user));
        }
        return userResponseList;
    }
}
