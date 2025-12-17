package ir.intellij.onlineexaminationmanagement.controller;

import ir.intellij.onlineexaminationmanagement.dto.UserResponseDto;
import ir.intellij.onlineexaminationmanagement.model.Role;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.model.UserStatus;
import ir.intellij.onlineexaminationmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam("username") String username,
                               @RequestParam("status") String status) {
        User user = userService.findByUsername(username);
        user.setUserStatus(UserStatus.valueOf(status));
        userService.save(user);
        return "pending-users";
    }

    @GetMapping("/edit/{username}")
    public String edit(@PathVariable("username") String username, Model model) {
        User user = userService.findByUsername(username);
        UserResponseDto responseDto = userService.entityToResponseDto(user);
        model.addAttribute("user", responseDto);
        return "edit-user";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute UserResponseDto userResponseDto,
                             RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(userResponseDto.getUsername());
        user.setFullName(userResponseDto.getFullName());
        user.setPhoneNumber(userResponseDto.getPhoneNumber());
        user.setAge(userResponseDto.getAge());
        user.setRole(Role.valueOf(userResponseDto.getRole()));
        user.setUserStatus(UserStatus.valueOf(userResponseDto.getStatus()));
        userService.save(user);
        redirectAttributes.addFlashAttribute("updateMessage", user.getUsername() + " updated successfully");
        return "redirect:/dashboard/all-users";
    }

    @GetMapping("/delete/{username}")
    public String delete(@PathVariable("username") String username,
                         RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(username);
        userService.delete(user);
        redirectAttributes.addFlashAttribute("deleteMessage", user.getUsername() + " deleted successfully");
        return "redirect:/dashboard/all-users";
    }
}
