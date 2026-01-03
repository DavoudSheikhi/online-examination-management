package ir.intellij.onlineexaminationmanagement.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue(value = "DESCRIPTIVE_QUESTION")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class DescriptiveQuestion extends Question {


}
