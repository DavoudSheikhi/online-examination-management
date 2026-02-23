package ir.intellij.onlineexaminationmanagement.dto.attempt;

public record AttemptSaveAnswerRequest(
        Long examQuestionId,
        Long selectedExamOptionId,
        String descriptiveText
) {
}

