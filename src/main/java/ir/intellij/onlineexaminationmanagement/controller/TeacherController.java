package ir.intellij.onlineexaminationmanagement.controller;

import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.model.ExamAttempt;
import ir.intellij.onlineexaminationmanagement.model.ExamAttemptStatus;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.repository.ExamAttemptRepository;
import ir.intellij.onlineexaminationmanagement.security.CustomUserDetails;
import ir.intellij.onlineexaminationmanagement.service.CourseService;
import ir.intellij.onlineexaminationmanagement.service.ScoreService;
import ir.intellij.onlineexaminationmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {
    private final CourseService courseService;
    private final UserService userService;
    private final ExamAttemptRepository examAttemptRepository;
    private final ScoreService scoreService;

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/courses")
    public String myCourses(@AuthenticationPrincipal CustomUserDetails user,
                            Model model) {
        User teacher = userService.findByUsername(user.getUsername());
        List<Course> coursesByTeacher = courseService.findCoursesByTeacher(teacher);
        model.addAttribute("courses", coursesByTeacher);
        model.addAttribute("teacher", teacher);
        return "teacher-courses";
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/course/{courseCode}/exam/{examCode}/attempts")
    public String examAttempts(@AuthenticationPrincipal CustomUserDetails teacher,
                               @PathVariable String courseCode,
                               @PathVariable String examCode,
                               Model model) {
        List<ExamAttempt> attempts = examAttemptRepository.findAttemptsForTeacherExam(courseCode, examCode, teacher.getUsername());

        Instant now = Instant.now();
        for (ExamAttempt a : attempts) {
            if (a.getStatus() == ExamAttemptStatus.IN_PROGRESS
                    && a.getEndsAt() != null
                    && !now.isBefore(a.getEndsAt())) {
                a.setStatus(ExamAttemptStatus.TIME_EXPIRED);
                examAttemptRepository.save(a);
                scoreService.finalizeAutoScoring(a);
            }
        }
        model.addAttribute("attempts", attempts);
        model.addAttribute("courseCode", courseCode);
        model.addAttribute("examCode", examCode);
        return "exam-attempts";
    }
}
