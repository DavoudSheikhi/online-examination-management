package ir.intellij.onlineexaminationmanagement.controller;

import ir.intellij.onlineexaminationmanagement.dto.UserRequestDto;
import ir.intellij.onlineexaminationmanagement.dto.UserResponseDto;
import ir.intellij.onlineexaminationmanagement.service.UserService;
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
    public String register(@ModelAttribute UserRequestDto userRequestDto, RedirectAttributes redirectAttributes) {
        UserResponseDto userResponseDto = userService.saveEntityFromDto(userRequestDto);
        redirectAttributes.addFlashAttribute("registerMessage", userResponseDto.getUsername() + " registered successfully");
        return "redirect:/welcome";
    }

}
