package ir.intellij.onlineexaminationmanagement.controller;

import ir.intellij.onlineexaminationmanagement.dto.CourseRequestDto;
import ir.intellij.onlineexaminationmanagement.dto.CourseResponseDto;
import ir.intellij.onlineexaminationmanagement.mapper.CourseMapper;
import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;


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


}
