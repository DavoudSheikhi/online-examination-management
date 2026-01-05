package ir.intellij.onlineexaminationmanagement.service.impl;

import ir.intellij.onlineexaminationmanagement.dto.question.QuestionResponseDTO;
import ir.intellij.onlineexaminationmanagement.mapper.QuestionMapper;
import ir.intellij.onlineexaminationmanagement.model.*;
import ir.intellij.onlineexaminationmanagement.repository.*;
import ir.intellij.onlineexaminationmanagement.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuestionMapper questionMapper;
    private final ExamRepository examRepository;
    private final ExamQuestionRepository examQuestionRepository;

    @Override
    public List<Question> findByCourseAndCreator(Course course, User teacher) {
        return questionRepository.findAllQuestionsByCourseAndCreator(course, teacher);
    }

    @Override
    public List<QuestionResponseDTO> getQuestionBank(String courseCode, String username) {

        List<Question> byCourseAndCreator = findByCourseAndCreator(courseRepository.findByCourseCode(courseCode), userRepository.findByUsername(username));
        return questionMapper.toResponseDTOList(byCourseAndCreator);
    }

    @Override
    @Transactional
    public void addDescriptive(User teacher, String courseCode, String examCode, String title, String text, String score) {
        Course byCourseCode = courseRepository.findByCourseCode(courseCode);
        DescriptiveQuestion descriptiveQuestion = DescriptiveQuestion.builder()
                .title(title)
                .text(text)
                .course(byCourseCode)
                .creatorUsername(teacher.getUsername())
                .questionType(QuestionType.DESCRIPTIVE)
                .creator(teacher)
                .build();

        Question saved = questionRepository.save(descriptiveQuestion);

        Exam exam = examRepository.findByExamCode(examCode);
        ExamQuestion examQuestion = ExamQuestion.builder()
                .question(saved)
                .exam(exam)
                .score(Double.valueOf(score))
                .build();

        examQuestionRepository.save(examQuestion);
    }

    @Override
    public Question findByTitle(String title) {
        return questionRepository.findByTitle(title);
    }
}
