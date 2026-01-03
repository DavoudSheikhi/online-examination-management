package ir.intellij.onlineexaminationmanagement.dto.question;

public record QuestionResponseDTO(
        String title,
        String text,
        String creatorUsername,
        String courseCode
) {
};
