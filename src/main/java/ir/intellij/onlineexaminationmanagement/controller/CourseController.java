package ir.intellij.onlineexaminationmanagement.controller;

import ir.intellij.onlineexaminationmanagement.dto.course.CourseRequestDto;
import ir.intellij.onlineexaminationmanagement.dto.course.CourseResponseDto;
import ir.intellij.onlineexaminationmanagement.dto.question.QuestionResponseDTO;
import ir.intellij.onlineexaminationmanagement.model.*;
import ir.intellij.onlineexaminationmanagement.security.CustomUserDetails;
import ir.intellij.onlineexaminationmanagement.service.CourseService;
import ir.intellij.onlineexaminationmanagement.service.ExamService;
import ir.intellij.onlineexaminationmanagement.service.QuestionService;
import ir.intellij.onlineexaminationmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;
    private final ExamService examService;
    private final QuestionService questionService;


    @GetMapping("/all-courses")
    public String course(Model model) {
        List<Course> all = courseService.findAll();
        List<CourseResponseDto> responseDtoList = courseService.entityToResponseList(all);
        model.addAttribute("courses", responseDtoList);
        return "all-courses";
    }

    @PostMapping("/add")
    public String addCourse(@ModelAttribute CourseRequestDto dto) {

        courseService.saveCourseFromDto(dto);

        return "redirect:/course/all-courses";
    }

    @PostMapping("/delete/{courseCode}")
    public String deleteCourse(@PathVariable String courseCode) {
        courseService.deleteCourseByCourseCode(courseCode);
        return "redirect:/course/all-courses";
    }

    @GetMapping("/edit/{courseCode}")
    public String editCourse(@PathVariable String courseCode, Model model) {
        Course byCourseCode = courseService.findByCourseCode(courseCode);
        CourseResponseDto courseResponseDto = courseService.entityToDto(byCourseCode);
        model.addAttribute("responseDto", courseResponseDto);
        return "course-edit";
    }

    @PostMapping("/edit/{courseCode}")
    public String updateCourse(@PathVariable String courseCode,
                               @ModelAttribute CourseRequestDto dto) {
        courseService.updateCourse(courseCode, dto);
        return "redirect:/course/all-courses";
    }

    @GetMapping("/info/{courseCode}")
    public String courseInfo(@PathVariable String courseCode,
                             Model model) {
        Course byCourseCode = courseService.findByCourseCode(courseCode);
//ToDo ( need dto)
        model.addAttribute("course", byCourseCode);
        return "course-info";
    }

    @GetMapping("/{courseCode}/assign-teacher")
    public String assignTeacher(@PathVariable String courseCode,
                                Model model) {
        Course byCourseCode = courseService.findByCourseCode(courseCode);
        List<User> allTeachers = userService.findAllApprovedTeachers();
        model.addAttribute("course", byCourseCode);
        model.addAttribute("allTeachers", allTeachers);
        return "assign-teacher";
    }

    @PostMapping("/{courseCode}/assign-teacher")
    public String assignTeacherSubmit(@PathVariable String courseCode,
                                      @RequestParam String username) {

        courseService.assignTeacher(courseCode, username);

        return "redirect:/course/info/" + courseCode;
    }

    @PostMapping("/{courseCode}/remove-teacher")
    public String removeTeacher(@PathVariable String courseCode,
                                Model model) {
        Course byCourseCode = courseService.findByCourseCode(courseCode);
        if (byCourseCode.getTeacher() == null) {
            model.addAttribute("teacherIsEmpty", true);
            model.addAttribute("course", byCourseCode);
            return "course-info";
        }
        courseService.deleteTeacherFromCourse(byCourseCode);
        Course findCourse = courseService.findByCourseCode(courseCode);
        model.addAttribute("successDelete", true);
        model.addAttribute("course", findCourse);
        return "course-info";
    }


    @GetMapping("/{courseCode}/add-student")
    public String addStudent(@PathVariable String courseCode,
                             Model model) {
        Course byCourseCode = courseService.findByCourseCode(courseCode);
        List<User> allStudents = userService.findEligibleStudents(courseCode, Role.STUDENT, UserStatus.APPROVED);
        model.addAttribute("course", byCourseCode);
        model.addAttribute("allStudents", allStudents);
        return "add-student";
    }

    @PostMapping("/{courseCode}/add-student")
    public String addStudentSubmit(@PathVariable String courseCode,
                                   @RequestParam String username,
                                   RedirectAttributes redirectAttributes) {
        Course byCourseCode = courseService.findByCourseCode(courseCode);
        User byUsername = userService.findByUsername(username);
        courseService.addStudent(byCourseCode, byUsername);

        redirectAttributes.addFlashAttribute("addStudentSuccess", byUsername.getUsername() + " با موفقیت به درس اضافه گردید ");

        return "redirect:/course/" + courseCode + "/add-student";
    }

    @GetMapping("{courseCode}/enrolled-students")
    public String enrolledStudents(@PathVariable String courseCode,
                                   Model model) {
        Course byCourseCode = courseService.findByCourseCode(courseCode);
        Set<User> enrolledStudents = byCourseCode.getEnrolledStudents();
        model.addAttribute("course", byCourseCode);
        model.addAttribute("enrolledStudents", enrolledStudents);
        return "enrolled-students";
    }

    @PostMapping("/{courseCode}/remove-student")
    public String removeStudent(@PathVariable String courseCode,
                                @RequestParam String username,
                                RedirectAttributes redirectAttributes) {
        Course byCourseCode = courseService.findByCourseCode(courseCode);
        User byUsername = userService.findByUsername(username);
        courseService.removeStudent(byCourseCode, byUsername);
        redirectAttributes.addFlashAttribute("removeSuccess", byUsername.getUsername() + " با موفقیت از درس مربوطه اضافه گردید ");

        return "redirect:/course/" + courseCode + "/enrolled-students";
    }

    @GetMapping("/users/{courseCode}")
    public String courseUsers(@PathVariable String courseCode, Model model) {
        Course course = courseService.findByCourseCode(courseCode);
        List<User> allUsersInCourse = userService.findAllUsersInCourse(courseCode);
        model.addAttribute("course", course);
        model.addAttribute("allUsersInCourse", allUsersInCourse);
        return "course-users";
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/{courseCode}/exams")
    public String courseExams(@PathVariable String courseCode, Model model) {
        Course course = courseService.findByCourseCode(courseCode);
        List<Exam> exams = examService.findByCourse(course);

        model.addAttribute("exams", exams);
        model.addAttribute("course", course);
        return "course-exams";

    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/{courseCode}/ownExams")
    public String ownExams(@AuthenticationPrincipal CustomUserDetails teacher,
                           @PathVariable String courseCode,
                           Model model) {
        Course course = courseService.findByCourseCode(courseCode);
        List<Exam> ownExams = examService.findByCourseCodeAndUsername(courseCode, teacher.getUsername());

        model.addAttribute("course", course);
        model.addAttribute("ownExams", ownExams);
        return "course-own-exams";

    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/{courseCode}/exams/new")
    public String addNewExam(@AuthenticationPrincipal CustomUserDetails teacher,
                             @PathVariable String courseCode,
                             @RequestParam String title,
                             @RequestParam String description,
                             @RequestParam String startDate,
                             @RequestParam String durationInMinutes,
                             RedirectAttributes redirectAttributes) {
        User byUsername = userService.findByUsername(teacher.getUsername());
        Course course = courseService.findByCourseCode(courseCode);
        Exam exam = Exam.builder()
                .title(title)
                .description(description)
                .startDate(LocalDate.parse(startDate))
                .durationInMinutes(Integer.parseInt(durationInMinutes))
                .course(course)
                .createdBy(byUsername)
                .creatorUsername(byUsername.getUsername())
                .build();

        Exam savedExam = examService.save(exam);
        redirectAttributes.addFlashAttribute("addNewExamSuccess", "the exam:" + savedExam.getExamCode() + " added successfully");
        return "redirect:/course/" + courseCode + "/exams";
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/{courseCode}/exam/{examCode}/questionBank/add/")
    public String addFromCourseQuestionBank(
            @AuthenticationPrincipal CustomUserDetails teacher,
            @PathVariable String courseCode,
            @PathVariable String examCode,
            Model model) {
        List<QuestionResponseDTO> questionBankResponse = questionService.getQuestionBank(courseCode, teacher.getUsername());


        model.addAttribute("questionBank", questionBankResponse);
        model.addAttribute("courseCode", courseCode);
        model.addAttribute("examCode", examCode);
        return "question-bank";
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/{courseCode}/exam/{examCode}/question/new/")
    public String addNewQuestion(
            @PathVariable String courseCode,
            @PathVariable String examCode,
            @RequestParam String questionType,
            Model model) {
        System.out.println("questionType: " + questionType);
        if (questionType.equals(QuestionType.MULTIPLE_CHOICE.name())) {
            model.addAttribute("courseCode", courseCode);
            model.addAttribute("examCode", examCode);
            return "add-multiple-choice-question";
        } else {
            model.addAttribute("courseCode", courseCode);
            model.addAttribute("examCode", examCode);
            return "add-descriptive-question";
        }
    }
}

