package ir.intellij.onlineexaminationmanagement.mapper;

import ir.intellij.onlineexaminationmanagement.dto.question.QuestionResponseDTO;
import ir.intellij.onlineexaminationmanagement.model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(source = "course.courseCode", target = "courseCode")
    QuestionResponseDTO toResponseDTO(Question question);

    List<QuestionResponseDTO> toResponseDTOList(List<Question> questions);

}
