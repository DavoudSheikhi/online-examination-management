package ir.intellij.onlineexaminationmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"attempt_id", "exam_question_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AttemptAnswer extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attempt_answer_seq")
    @SequenceGenerator(name = "attempt_answer_seq", sequenceName = "attempt_answer_seq", allocationSize = 5)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attempt_id", nullable = false)
    private ExamAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_question_id", nullable = false)
    private ExamQuestion examQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_exam_option_id")
    private ExamOption selectedExamOption;

    @Column(length = 2000)
    private String descriptiveText;

    private Instant answeredAt;

    private Instant lastUpdatedAt;

    @Column(nullable = false)
    private Double autoScore = 0d;

    @Column(nullable = false)
    private Double manualScore = 0d;

    @Column(nullable = false)
    private Double finalScore = 0d;
}

