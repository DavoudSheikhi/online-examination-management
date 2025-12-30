package ir.intellij.onlineexaminationmanagement.controller;

import ir.intellij.onlineexaminationmanagement.dto.UserRegisterDTO;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {
    private final UserService userService;

    @PostMapping
    public String register(@Valid @ModelAttribute UserRegisterDTO userRegisterDto,
                           RedirectAttributes redirectAttributes) {
        User registered = userService.register(userRegisterDto);

        redirectAttributes.addFlashAttribute("registerMessage", registered.getUsername() + " registered successfully");
        return "redirect:/welcome";
    }
}
