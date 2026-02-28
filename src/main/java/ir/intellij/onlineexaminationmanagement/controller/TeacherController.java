package ir.intellij.onlineexaminationmanagement.controller;

import ir.intellij.onlineexaminationmanagement.model.*;
import ir.intellij.onlineexaminationmanagement.repository.AttemptAnswerRepository;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final AttemptAnswerRepository attemptAnswerRepository;

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

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/attempt/{attemptId}")
    public String attemptDetail(@AuthenticationPrincipal CustomUserDetails teacher,
                                @PathVariable Long attemptId,
                                Model model) {
        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found: " + attemptId));

        scoreService.finalizeAutoScoring(attempt);

        List<AttemptAnswer> answers = attemptAnswerRepository.findAllByAttemptIdWithQuestion(attemptId);
        model.addAttribute("attempt", attempt);
        model.addAttribute("answers", answers);
        return "attempt-detail-grade";
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/attempt-answer/{attemptAnswerId}/score")
    public String scoreDescriptive(@AuthenticationPrincipal CustomUserDetails teacher,
                                   @PathVariable Long attemptAnswerId,
                                   @RequestParam("score") double score,
                                   RedirectAttributes redirectAttributes) {
        AttemptAnswer aa = attemptAnswerRepository.findById(attemptAnswerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found: " + attemptAnswerId));
        Long attemptId = aa.getAttempt() == null ? null : aa.getAttempt().getId();

        scoreService.setManualScore(attemptAnswerId, score, teacher.getUsername());
        redirectAttributes.addFlashAttribute("scoreSaved", "نمره ثبت شد");
        return "redirect:/teacher/attempt/" + attemptId;
    }
}
