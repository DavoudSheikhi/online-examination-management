package ir.intellij.onlineexaminationmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ExamOption {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_option_seq")
    @SequenceGenerator(name = "exam_option_seq", sequenceName = "exam_option_seq", allocationSize = 5)
    private Long id;

    @NotBlank
    private String text;
}
