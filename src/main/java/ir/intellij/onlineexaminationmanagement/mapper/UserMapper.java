package ir.intellij.onlineexaminationmanagement.mapper;

import ir.intellij.onlineexaminationmanagement.dto.user.UserRegisterDTO;
import ir.intellij.onlineexaminationmanagement.dto.user.UserResponseDto;
import ir.intellij.onlineexaminationmanagement.dto.user.UserUpdateInfoDTO;
import ir.intellij.onlineexaminationmanagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRegisterDTO dto);

    void updateUser(UserRegisterDTO userRegisterDTO,
                    @MappingTarget User user);

    UserResponseDto entityToResponse(User user);

    List<UserResponseDto> entityListToResponseList(List<User> users);

    void updateUserInfo(UserUpdateInfoDTO userUpdateInfoDTO,
                        @MappingTarget User user);
}
