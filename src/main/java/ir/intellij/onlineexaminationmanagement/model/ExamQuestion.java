package ir.intellij.onlineexaminationmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@DiscriminatorValue(value = "EXAM_QUESTION")
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public abstract class ExamQuestion extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_question_seq")
    @SequenceGenerator(name = "exam_question_seq", sequenceName = "exam_question_seq", allocationSize = 5)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;
    @Column(nullable = false)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType;

    @ManyToOne
    private User creator;
    @Column(nullable = false)
    private String creatorUsername;


    @ManyToOne
    private Exam exam;

    private Double score;

}
