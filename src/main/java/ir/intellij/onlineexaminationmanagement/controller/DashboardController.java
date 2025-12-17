package ir.intellij.onlineexaminationmanagement.controller;


import ir.intellij.onlineexaminationmanagement.dto.UserResponseDto;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.model.UserStatus;
import ir.intellij.onlineexaminationmanagement.security.CustomUserDetails;
import ir.intellij.onlineexaminationmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final UserService userService;


    //    After login go to dashboard.html
    @GetMapping
    public String dashboard(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("user", user);
        return "dashboard";
    }

    @GetMapping("/pending-users")
    public String pendingUsers(Model model) {
        List<User> pendingUsers = userService.findUsersByUserStatus(UserStatus.PENDING);
        List<UserResponseDto> responseDtoList = userService.entityListToResponseList(pendingUsers);
        model.addAttribute("pendingUsers", responseDtoList);
        return "pending-users";
    }

    @GetMapping("/all-users")
    public String allUsers(Model model) {
        List<User> all = userService.findAll();
        List<UserResponseDto> responseDtoList = userService.entityListToResponseList(all);
        model.addAttribute("allUsers", responseDtoList);
        return "all-users";
    }
}
