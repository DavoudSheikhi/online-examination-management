package ir.intellij.onlineexaminationmanagement.dto.option;

import java.util.List;

public record CreateMultipleChoiceQuestionDto(
        String title,
        String text,
        String score,
        List<OptionDto> options,
        Integer correctOption
) {
};
