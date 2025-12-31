package ir.intellij.onlineexaminationmanagement.dto.user;

import jakarta.validation.constraints.*;

public record UserRegisterDTO(
        @NotBlank(message = "نام و نام خانوادگی الزامی است")
        @Size(min = 3, max = 40, message = "نام باید بین 3 تا 40 کاراکتر باشد")
        @Pattern(
                regexp = "^[\\p{L}]+(\\s[\\p{L}]+)*$",
                message = "نام فقط می‌تواند شامل حروف و یک فاصله بین کلمات باشد"
        )
        String fullName,

        @NotBlank(message = "شماره موبایل الزامی است")
        @Pattern(
                regexp = "^09\\d{9}$",
                message = "شماره موبایل باید با 09 شروع شود و 11 رقم باشد"
        )
        String phoneNumber,

        @NotNull(message = "سن الزامی است")
        @Min(value = 18, message = "سن باید حداقل 18 سال باشد")
        @Max(value = 100, message = "سن باید حداکثر 100 سال باشد")
        Integer age,

        @NotBlank(message = "نام کاربری الزامی است")
        @Size(min = 4, max = 20, message = "نام کاربری باید بین 4 تا 20 کاراکتر باشد")
        @Pattern(
                regexp = "^[a-zA-Z0-9]+$",
                message = "نام کاربری فقط می‌تواند شامل حروف و اعداد باشد"
        )
        String username,

        @NotBlank(message = "رمز عبور الزامی است")
        @Size(min = 4, message = "رمز عبور باید حداقل 4 کاراکتر باشد")
        String password,

        @NotBlank(message = "نقش الزامی است")
        @Pattern(
                regexp = "^(STUDENT|TEACHER)$",
                message = "نقش باید STUDENT یا TEACHER باشد"
        )
        String role
) {
}
