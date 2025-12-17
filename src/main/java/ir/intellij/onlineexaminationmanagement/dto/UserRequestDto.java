package ir.intellij.onlineexaminationmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserRequestDto {
    private String fullName;
    private String phoneNumber;
    private Integer age;

    private String username;
    private String password;
    private String role;
}
