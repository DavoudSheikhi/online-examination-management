package ir.intellij.onlineexaminationmanagement.security;

import ir.intellij.onlineexaminationmanagement.model.Role;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.model.UserStatus;
import ir.intellij.onlineexaminationmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        List<User> managers = userRepository.findUserByRole(Role.MANAGER);
        if (!managers.isEmpty()) {
            userRepository.deleteAll(managers);
        }

        User manager = User.builder()
                .fullName("Manager")
                .age(45)
                .phoneNumber("09101234567")
                .username("Admin")
                .password("{noop}admin")
                .role(Role.MANAGER)
                .userStatus(UserStatus.APPROVED)
                .build();

        userRepository.save(manager);
    }
}
