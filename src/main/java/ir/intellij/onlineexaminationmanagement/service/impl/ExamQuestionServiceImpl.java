package ir.intellij.onlineexaminationmanagement.service.impl;

import ir.intellij.onlineexaminationmanagement.model.*;
import ir.intellij.onlineexaminationmanagement.repository.ExamOptionRepository;
import ir.intellij.onlineexaminationmanagement.repository.ExamQuestionRepository;
import ir.intellij.onlineexaminationmanagement.repository.ExamRepository;
import ir.intellij.onlineexaminationmanagement.repository.QuestionRepository;
import ir.intellij.onlineexaminationmanagement.service.ExamQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamQuestionServiceImpl implements ExamQuestionService {
    private final ExamRepository examRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final QuestionRepository questionRepository;
    private final ExamOptionRepository examOptionRepository;

    @Override
    public List<ExamQuestion> findExamQuestions(String examCode) {
        Exam byExamCode = examRepository.findByExamCode(examCode);
        return examQuestionRepository.findByExam(byExamCode);
    }

    @Override
    public ExamQuestion save(ExamQuestion examQuestion) {
        return examQuestionRepository.save(examQuestion);
    }

    @Override
    @Transactional
    public void addExamQuestionFromBank(String examCode, String questionTitle, Double score) {
        Exam byExamCode = examRepository.findByExamCode(examCode);
        Question question = questionRepository.findByTitle(questionTitle);

        if (question.getQuestionType() == QuestionType.DESCRIPTIVE) {
            ExamQuestion examQuestion = ExamDescriptiveQuestion.builder()
                    .title(questionTitle)
                    .text(question.getText())
                    .questionType(question.getQuestionType())
                    .creator(question.getCreator())
                    .creatorUsername(question.getCreator().getUsername())
                    .exam(byExamCode)
                    .score(score)
                    .build();
            examQuestionRepository.save(examQuestion);
        } else if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
            MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) question;

            List<Option> options = multipleChoiceQuestion.getOptions();
            List<ExamOption> examOptions = new ArrayList<>();
            ExamOption correctExamOption = new ExamOption();

            for (Option option : options) {
                ExamOption examOption = new ExamOption();
                examOption.setText(option.getText());
                examOptionRepository.save(examOption);
                examOptions.add(examOption);
                if (option.equals(multipleChoiceQuestion.getCorrectOption())) {
                    correctExamOption = examOption;
                }
            }

            ExamQuestion examQuestion = ExamMultipleChoiceQuestion.builder()
                    .title(questionTitle)
                    .text(question.getText())
                    .questionType(question.getQuestionType())
                    .creator(question.getCreator())
                    .creatorUsername(question.getCreator().getUsername())
                    .exam(byExamCode)
                    .score(score)
                    .examOptions(examOptions)
                    .correctExamOption(correctExamOption)
                    .build();
            examQuestionRepository.save(examQuestion);
        }
    }
}
