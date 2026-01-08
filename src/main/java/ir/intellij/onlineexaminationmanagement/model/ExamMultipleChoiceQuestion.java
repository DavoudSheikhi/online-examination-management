package ir.intellij.onlineexaminationmanagement.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@DiscriminatorValue(value = "EXAM_M_C_QUESTION")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class ExamMultipleChoiceQuestion extends ExamQuestion {
    @OneToMany
//            (cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamOption> examOptions;
    @ManyToOne
    private ExamOption correctExamOption;
}
