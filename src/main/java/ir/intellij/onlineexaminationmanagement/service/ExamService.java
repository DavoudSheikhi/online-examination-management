package ir.intellij.onlineexaminationmanagement.service;

import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.model.Exam;

import java.util.List;

public interface ExamService {
    List<Exam> findByCourse(Course course);

    List<Exam> findByCourseCodeAndUsername(String courseCode, String username);

    Exam save(Exam exam);

    Exam findByExamCode(String examCode);

    void delete(Exam exam);
}