package ir.intellij.onlineexaminationmanagement.controller;

import ir.intellij.onlineexaminationmanagement.dto.attempt.AttemptQuestionView;
import ir.intellij.onlineexaminationmanagement.dto.attempt.AttemptSaveAnswerRequest;
import ir.intellij.onlineexaminationmanagement.model.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                            @PathVariable String examCode) {
        ExamAttempt attempt = examAttemptService.startOrResumeAttempt(examCode, user.getUsername());
        return "redirect:/student/attempt/" + attempt.getId();
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/attempt/{attemptId}")
    public String attemptRunner(@AuthenticationPrincipal CustomUserDetails user,
                                @PathVariable Long attemptId,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        long remainingSeconds = examAttemptService.getRemainingSeconds(attemptId, user.getUsername());

        ExamAttempt attempt = examAttemptService.findAttemptForStudent(attemptId, user.getUsername());
        if (remainingSeconds <= 0 || attempt.getStatus() != ExamAttemptStatus.IN_PROGRESS) {
            redirectAttributes.addFlashAttribute("examClosed", "زمان آزمون به پایان رسیده یا آزمون قبلا ثبت شده است.");
            return "redirect:/student/" + attempt.getExam().getCourse().getCourseCode() + "/exams";
        }
        List<AttemptAnswer> answers = examAttemptService.getAttemptAnswers(attemptId, user.getUsername());

        List<AttemptQuestionView> questions = answers.stream()
                .map(a -> toQuestionView(a))
                .toList();

        model.addAttribute("attemptId", attemptId);
        model.addAttribute("examCode", attempt.getExam().getExamCode());
        model.addAttribute("courseCode", attempt.getExam().getCourse().getCourseCode());
        model.addAttribute("remainingSeconds", remainingSeconds);
        model.addAttribute("questions", questions);
        return "attempt-runner";
    }

    @PreAuthorize("hasRole('STUDENT')")
    @ResponseBody
    @PostMapping("/attempt/{attemptId}/answer")
    public AttemptQuestionView.SaveResponse autosaveAnswer(@AuthenticationPrincipal CustomUserDetails user,
                                                           @PathVariable Long attemptId,
                                                           @RequestBody AttemptSaveAnswerRequest request) {
        examAttemptService.autosaveAnswer(
                attemptId,
                user.getUsername(),
                request.examQuestionId(),
                request.selectedExamOptionId(),
                request.descriptiveText()
        );
        long remainingSeconds = examAttemptService.getRemainingSeconds(attemptId, user.getUsername());
        return new AttemptQuestionView.SaveResponse(true, remainingSeconds);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @ResponseBody
    @PostMapping("/attempt/{attemptId}/heartbeat")
    public AttemptQuestionView.SaveResponse heartbeat(@AuthenticationPrincipal CustomUserDetails user,
                                                      @PathVariable Long attemptId) {
        long remainingSeconds = examAttemptService.getRemainingSeconds(attemptId, user.getUsername());
        return new AttemptQuestionView.SaveResponse(true, remainingSeconds);
    }



    private AttemptQuestionView toQuestionView(AttemptAnswer a) {
        ExamQuestion q = a.getExamQuestion();
        List<AttemptQuestionView.OptionView> options = List.of();
        if (q instanceof ExamMultipleChoiceQuestion mcq) {
            options = mcq.getExamOptions().stream()
                    .map(o -> new AttemptQuestionView.OptionView(o.getId(), o.getText()))
                    .toList();
        }
        Long selectedOptionId = a.getSelectedExamOption() == null ? null : a.getSelectedExamOption().getId();
        return new AttemptQuestionView(
                a.getId(),
                q.getId(),
                q.getTitle(),
                q.getText(),
                q.getQuestionType(),
                q.getScore(),
                options,
                selectedOptionId,
                a.getDescriptiveText()
        );
    }
}
