package ir.intellij.onlineexaminationmanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@DiscriminatorValue(value = "M_C_QUESTION")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class MultipleChoiceQuestion extends Question {

    @OneToMany
//            (cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;

    @ManyToOne
    private Option correctOption;
}
