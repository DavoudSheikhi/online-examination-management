package ir.intellij.onlineexaminationmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/welcome")
public class WelcomeController {

    @GetMapping
    public String welcome(@RequestParam(value = "logout", required = false) String logout,
                          @RequestParam(value = "loginError", required = false) String login,
                          Model model) {
        if (logout != null) {
            model.addAttribute("logoutMessage", "You are logged out");
        }
        if (login != null) {
            model.addAttribute("loginError", "username or password are incorrect");
        }
        return "welcome";
    }
}
