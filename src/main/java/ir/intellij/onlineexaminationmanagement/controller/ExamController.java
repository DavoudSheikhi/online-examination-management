package ir.intellij.onlineexaminationmanagement.controller;

import ir.intellij.onlineexaminationmanagement.model.*;
import ir.intellij.onlineexaminationmanagement.service.CourseService;
import ir.intellij.onlineexaminationmanagement.service.ExamQuestionService;
import ir.intellij.onlineexaminationmanagement.service.ExamService;
import ir.intellij.onlineexaminationmanagement.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    private final CourseService courseService;

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/course/{courseCode}/exams/edit/{examCode}")
    public String editExam(@PathVariable String courseCode,
                           @PathVariable String examCode,
                           Model model) {
        Course course = courseService.findByCourseCode(courseCode);
        Exam exam = examService.findByExamCode(examCode);
        model.addAttribute("course", course);
        model.addAttribute("exam", exam);
        return "edit-exam";
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/course/{courseCode}/exams/edit/{examCode}")
    public String editExam(@PathVariable String courseCode,
                           @PathVariable String examCode,
                           @RequestParam String startDate,
                           @RequestParam String durationInMinutes,
                           @RequestParam String description,
                           RedirectAttributes redirectAttributes) {
        Exam exam = examService.findByExamCode(examCode);
        exam.setStartDate(LocalDate.parse(startDate));
        exam.setDurationInMinutes(Integer.parseInt(durationInMinutes));
        exam.setDescription(description);
        examService.save(exam);
        redirectAttributes.addFlashAttribute("examEditSuccess", "آزمون: " + exam.getExamCode() + " با موفقیت ویرایش شد");
        return "redirect:/course/" + courseCode + "/ownExams";
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/course/{courseCode}/exams/delete/{examCode}")
    public String deleteExam(@PathVariable String courseCode,
                             @PathVariable String examCode,
                             RedirectAttributes redirectAttributes) {
        Exam exam = examService.findByExamCode(examCode);
        examService.delete(exam);
        redirectAttributes.addFlashAttribute("examDeleteSuccess", "آزمون: " + exam.getExamCode() + " با موفقیت حذف شد");
        return "redirect:/course/" + courseCode + "/ownExams";
    }


}