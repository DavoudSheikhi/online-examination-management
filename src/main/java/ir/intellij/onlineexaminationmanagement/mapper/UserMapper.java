package ir.intellij.onlineexaminationmanagement.mapper;

import ir.intellij.onlineexaminationmanagement.dto.UserRegisterDTO;
import ir.intellij.onlineexaminationmanagement.dto.UserResponseDto;
import ir.intellij.onlineexaminationmanagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRegisterDTO dto);

    void updateUser(UserRegisterDTO userRegisterDTO,
                    @MappingTarget User user);




    static UserResponseDto entityToResponse(User user) {

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
