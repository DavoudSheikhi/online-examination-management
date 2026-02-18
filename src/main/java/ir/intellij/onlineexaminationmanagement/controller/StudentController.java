package ir.intellij.onlineexaminationmanagement.controller;

import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.model.Exam;
import ir.intellij.onlineexaminationmanagement.model.ExamAttempt;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.security.CustomUserDetails;
import ir.intellij.onlineexaminationmanagement.service.CourseService;
import ir.intellij.onlineexaminationmanagement.service.ExamAttemptService;
import ir.intellij.onlineexaminationmanagement.service.ExamService;
import ir.intellij.onlineexaminationmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("student")
public class StudentController {
    private final CourseService courseService;
    private final UserService userService;
    private final ExamService examService;
    private final ExamAttemptService examAttemptService;

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/courses")
    public String myCourses(@AuthenticationPrincipal CustomUserDetails user,
                            Model model) {
        User student = userService.findByUsername(user.getUsername());
        List<Course> coursesByStudent = courseService.findCoursesByStudent(student);
        model.addAttribute("courses", coursesByStudent);
        model.addAttribute("teacher", student);
        return "student-courses";
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/{courseCode}/exams")
    public String myExams(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable String courseCode,
            Model model) {
        List<Exam> exams = examService.findAvailableExamsForStudent(courseCode, user.getUsername());
        model.addAttribute("exams", exams);
        model.addAttribute("courseCode", courseCode);
        return "student-course-exams";
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/exam/{examCode}/start")
    public String startExam(@AuthenticationPrincipal CustomUserDetails user,
                            @PathVariable String examCode){
        ExamAttempt attempt = examAttemptService.startOrResumeAttempt(examCode, user.getUsername());
        return "redirect:/student/attempt/" + attempt.getId();
    }
}
