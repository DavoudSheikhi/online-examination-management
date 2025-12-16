package ir.intellij.onlineexaminationmanagement.security;

import ir.intellij.onlineexaminationmanagement.model.Role;
import ir.intellij.onlineexaminationmanagement.model.Student;
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
        if (!managers.isEmpty()) {
            userRepository.deleteAll(managers);
        }

        User manager = new User(
                "Manager",
                "09101234567",
                45,
                "admin",
                passwordEncoder.encode("0000"),
                Role.MANAGER,
                UserStatus.APPROVED);

        userRepository.save(manager);
    }
}
