package ir.intellij.onlineexaminationmanagement.service;

import ir.intellij.onlineexaminationmanagement.dto.question.QuestionResponseDTO;
import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.model.DescriptiveQuestion;
import ir.intellij.onlineexaminationmanagement.model.Question;
import ir.intellij.onlineexaminationmanagement.model.User;

import java.util.List;

public interface QuestionService {

    List<Question> findByCourseAndCreator(Course course, User teacher);

    List<QuestionResponseDTO> getQuestionBank(String courseCode, String username);

    void addDescriptive(User teacher, String courseCode, String examCode, String title, String text,String score);

    Question findByTitle(String title);
}
