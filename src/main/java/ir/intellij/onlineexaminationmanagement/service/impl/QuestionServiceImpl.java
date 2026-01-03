package ir.intellij.onlineexaminationmanagement.service.impl;

import ir.intellij.onlineexaminationmanagement.dto.question.QuestionResponseDTO;
import ir.intellij.onlineexaminationmanagement.mapper.QuestionMapper;
import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.model.Question;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.repository.CourseRepository;
import ir.intellij.onlineexaminationmanagement.repository.QuestionRepository;
import ir.intellij.onlineexaminationmanagement.repository.UserRepository;
import ir.intellij.onlineexaminationmanagement.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuestionMapper questionMapper;

    @Override
    public List<Question> findByCourseAndCreator(Course course, User teacher) {
        return questionRepository.findAllQuestionsByCourseAndCreator(course, teacher);
    }

    @Override
    public List<QuestionResponseDTO> getQuestionBank(String courseCode, String username) {

        List<Question> byCourseAndCreator = findByCourseAndCreator(courseRepository.findByCourseCode(courseCode), userRepository.findByUsername(username));
        return questionMapper.toResponseDTOList(byCourseAndCreator);
    }
}
