package ir.intellij.onlineexaminationmanagement.controller;

import ir.intellij.onlineexaminationmanagement.dto.UserResponseDto;
import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.model.Role;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.model.UserStatus;
import ir.intellij.onlineexaminationmanagement.service.CourseService;
import ir.intellij.onlineexaminationmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CourseService courseService;

    @PostMapping("/update-pending-status")
    public String updatePendingStatus(@RequestParam("username") String username,
                                      @RequestParam("status") String status) {
        User user = userService.findByUsername(username);
        user.setUserStatus(UserStatus.valueOf(status));
        userService.save(user);
        return "redirect:/dashboard/pending-users";
    }

    @GetMapping("/edit/{username}")
    public String edit(@PathVariable("username") String username, Model model) {
        User user = userService.findByUsername(username);
        UserResponseDto responseDto = userService.entityToResponseDto(user);
        model.addAttribute("user", responseDto);
        return "edit-user";
    }

    @PostMapping("/update-info")
    public String updateUser(@RequestParam String username,
                             @RequestParam String fullName,
                             @RequestParam String phoneNumber,
                             @RequestParam Integer age,
                             RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(username);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setAge(age);
        userService.save(user);
        redirectAttributes.addFlashAttribute("updateInfoMessage", user.getUsername() + " updated successfully");
        return "redirect:/user/edit/" + username;
    }

    @PostMapping("/update-role")
    public String updateUserRole(@RequestParam String username,
                                 @RequestParam String role,
                                 RedirectAttributes redirectAttributes) {
        userService.changeRole(username, Role.valueOf(role));

        redirectAttributes.addFlashAttribute("updateRoleMessage", username + " role " + "successfully changed to " + role);
        return "redirect:/user/edit/" + username;
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam String username,
                               @RequestParam String status,
                               RedirectAttributes redirectAttributes) {
        userService.changeStatus(username, status);

        redirectAttributes.addFlashAttribute("updateStatusMessage", username + " status " + "successfully changed to " + status);
        return "redirect:/user/edit/" + username;
    }

    @GetMapping("/delete/{username}")
    public String delete(@PathVariable("username") String username,
                         RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(username);
        userService.delete(user);


        redirectAttributes.addFlashAttribute("deleteMessage", user.getUsername() + " deleted successfully");
        return "redirect:/dashboard/all-users";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String fullName,
                         @RequestParam(required = false) String username,
                         @RequestParam(required = false) String phoneNumber,
                         @RequestParam(required = false) Integer age,
                         @RequestParam(required = false) String role,
                         @RequestParam(required = false) String status,
                         Model model) {
        Role enumRole;
        if (role == null || role.isBlank()) {
            enumRole = null;
        } else {
            enumRole = Role.valueOf(role.toUpperCase());
        }

        UserStatus enumStatus;
        if (status == null || status.isBlank()) {
            enumStatus = null;
        } else {
            enumStatus = UserStatus.valueOf(status.toUpperCase());
        }

        List<User> searchList = userService.search(fullName, username, phoneNumber, age, enumRole, enumStatus);
        List<UserResponseDto> responseDtoList = userService.entityListToResponseList(searchList);
        model.addAttribute("users", responseDtoList);
        return "searched-users";

    }
}
