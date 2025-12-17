package ir.intellij.onlineexaminationmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {
    private String fullName;
    private String phoneNumber;
    private Integer age;

    private String username;
    private String role;
    private String status;
}
