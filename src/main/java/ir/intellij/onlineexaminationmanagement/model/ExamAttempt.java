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
                @UniqueConstraint(columnNames = {"exam_id", "student_id"})
        }
)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ExamAttempt extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attemptExam_seq")
    @SequenceGenerator(name = "attemptExam_seq", sequenceName = "attemptExam_seq", allocationSize = 5)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User student;
    private Instant startedAt;
    private Instant endsAt;
    private Instant submittedAt;

    @Enumerated(EnumType.STRING)
    private ExamAttemptStatus status;

    @Column(nullable = false)
    private Double totalAutoScore = 0d;

    @Column(nullable = false)
    private Double totalManualScore = 0d;

    @Column(nullable = false)
    private Double totalScore = 0d;

}
