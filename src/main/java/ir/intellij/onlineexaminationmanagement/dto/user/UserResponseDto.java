package ir.intellij.onlineexaminationmanagement.dto.user;

public record UserResponseDto(String fullName,
                              String phoneNumber,
                              Integer age,
                              String username,
                              String role,
                              String userStatus,
                              boolean active) {
}
