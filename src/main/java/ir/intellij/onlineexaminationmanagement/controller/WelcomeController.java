package ir.intellij.onlineexaminationmanagement.controller;

import jakarta.servlet.http.HttpSession;
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
                          HttpSession session,
                          Model model) {
        if (logout != null) {
            model.addAttribute("logoutMessage", "You are logged out");
        }

        Object loginError = session.getAttribute("loginError");
        if (loginError != null) {
            model.addAttribute("loginError", loginError);
            session.removeAttribute("loginError");
        }
        return "welcome";
    }
}
