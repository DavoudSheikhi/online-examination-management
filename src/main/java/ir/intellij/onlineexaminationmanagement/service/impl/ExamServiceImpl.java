package ir.intellij.onlineexaminationmanagement.service.impl;

import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.model.Exam;
import ir.intellij.onlineexaminationmanagement.repository.ExamRepository;
import ir.intellij.onlineexaminationmanagement.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;

    public static String generateExamCode(String title, LocalDate date) {
        String random = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
        return title + "-" + random;
    }

    @Override
    public List<Exam> findByCourse(Course course) {
        return examRepository.findByCourse(course);
    }

    @Override
    public List<Exam> findByCourseCodeAndUsername(String courseCode, String username) {
        return examRepository.findByCourse_CourseCodeAndCreatedBy_Username(courseCode, username);
    }

    @Override
    public Exam save(Exam exam) {
        exam.setExamCode(generateExamCode(exam.getTitle(), exam.getStartDate()));
        return examRepository.save(exam);
    }

    @Override
    public Exam findByExamCode(String examCode) {
        return examRepository.findByExamCode(examCode);
    }

    @Override
    public void delete(Exam exam) {
        examRepository.delete(exam);
    }
}
