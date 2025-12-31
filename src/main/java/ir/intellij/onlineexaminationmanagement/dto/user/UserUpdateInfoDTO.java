package ir.intellij.onlineexaminationmanagement.dto.user;

import jakarta.validation.constraints.*;

public record UserUpdateInfoDTO(
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
        Integer age) {
}
