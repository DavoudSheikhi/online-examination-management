package ir.intellij.onlineexaminationmanagement.repository;

import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.model.Question;
import ir.intellij.onlineexaminationmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllQuestionsByCourseAndCreator(Course course, User creator);

    Question findByTitle(String title);
}
