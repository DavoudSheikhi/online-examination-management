package ir.intellij.onlineexaminationmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ExamQuestion extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_question_seq")
    @SequenceGenerator(name = "exam_question_seq", sequenceName = "exam_question_seq", allocationSize = 5)
    private Long id;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Exam exam;

    private Double score;

}
