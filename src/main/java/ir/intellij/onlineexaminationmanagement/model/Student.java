package ir.intellij.onlineexaminationmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue(value = "STUDENT")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder


public class Student extends User {
    private String studentId;
}
