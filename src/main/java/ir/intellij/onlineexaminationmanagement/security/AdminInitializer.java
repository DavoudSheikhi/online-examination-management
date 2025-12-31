package ir.intellij.onlineexaminationmanagement.security;

import ir.intellij.onlineexaminationmanagement.model.Role;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.model.UserStatus;
import ir.intellij.onlineexaminationmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        List<User> managers = userRepository.findUserByRole(Role.MANAGER);
        if (managers.isEmpty()) {
            User manager = User.builder()
                    .fullName("Manager")
                    .phoneNumber("09101234567")
                    .age(45)
                    .username("admin")
                    .password(passwordEncoder.encode("0000"))
                    .role(Role.MANAGER)
                    .userStatus(UserStatus.APPROVED)
                    .active(true)
                    .build();
            userRepository.save(manager);
        }
    }
}
