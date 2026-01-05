package ir.intellij.onlineexaminationmanagement.dto.question;

import ir.intellij.onlineexaminationmanagement.model.QuestionType;

public record QuestionResponseDTO(
        String title,
        String text,
        String creatorUsername,
        String courseCode,
        QuestionType questionType
) {
};
