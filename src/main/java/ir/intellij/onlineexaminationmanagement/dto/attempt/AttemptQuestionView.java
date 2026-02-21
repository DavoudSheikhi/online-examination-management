package ir.intellij.onlineexaminationmanagement.dto.attempt;

import ir.intellij.onlineexaminationmanagement.model.QuestionType;

import java.util.List;

public record AttemptQuestionView(
        Long attemptAnswerId,
        Long examQuestionId,
        String title,
        String text,
        QuestionType questionType,
        Double maxScore,
        List<OptionView> options,
        Long selectedExamOptionId,
        String descriptiveText
) {
    public record OptionView(Long id, String text) {
    }

    public record SaveResponse(boolean ok, long remainingSeconds) {
    }
}

