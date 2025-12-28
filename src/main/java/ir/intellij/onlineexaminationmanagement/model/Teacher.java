package ir.intellij.onlineexaminationmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue(value = "TEACHER")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder

public class Teacher extends User {
    private String teacherId;
}
