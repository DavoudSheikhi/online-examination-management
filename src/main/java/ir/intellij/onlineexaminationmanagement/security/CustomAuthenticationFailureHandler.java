package ir.intellij.onlineexaminationmanagement.security;

import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.model.UserStatus;
import ir.intellij.onlineexaminationmanagement.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        String errorMessage = "...";

        if (exception instanceof BadCredentialsException) {
            errorMessage = "نام کاربری یا رمز عبور اشتباه است";
        } else if (exception instanceof DisabledException) {
            User byUsername = userRepository.findByUsername(username);

            if (byUsername == null) {
                errorMessage = "کاربری با این نام وجود ندارد";
            } else {
                if (!byUsername.isActive()) {
                    errorMessage = "حساب کاربری شما غیرفعال شده است";
                } else if (byUsername.getUserStatus() == UserStatus.PENDING) {
                    errorMessage = "حساب کاربری شما در انتظار تأیید است";
                } else if (byUsername.getUserStatus() == UserStatus.REJECTED) {
                    errorMessage = "حساب کاربری شما رد شده است";
                }
            }
        }
        request.getSession().setAttribute("loginError", errorMessage);
        response.sendRedirect("/welcome");
    }
}
