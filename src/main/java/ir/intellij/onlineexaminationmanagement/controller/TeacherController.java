package ir.intellij.onlineexaminationmanagement.controller;

import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.security.CustomUserDetails;
import ir.intellij.onlineexaminationmanagement.service.CourseService;
import ir.intellij.onlineexaminationmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {
    private final CourseService courseService;
    private final UserService userService;

    @GetMapping("/courses")
    public String myCourses(@AuthenticationPrincipal CustomUserDetails user,
                            Model model) {
        User teacher = userService.findByUsername(user.getUsername());
        List<Course> coursesByTeacher = courseService.findCoursesByTeacher(teacher);
        model.addAttribute("courses", coursesByTeacher);
        model.addAttribute("teacher", teacher);
        return "teacher-courses";


    }
}
