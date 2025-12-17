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
        return new User(dto.getFullName(),
                dto.getPhoneNumber(),
                dto.getAge(),
                dto.getUsername(),
                dto.getPassword(),
                Role.valueOf(dto.getRole()),
                UserStatus.PENDING);
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
