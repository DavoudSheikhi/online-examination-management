package ir.intellij.onlineexaminationmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Exam extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_seq")
    @SequenceGenerator(name = "exam_seq", sequenceName = "exam_seq", allocationSize = 5)
    private Long id;
    @Column(nullable = false, unique = true)
    private String examCode;
    private String title;
    private String description;
    private Integer durationInMinutes;
    private LocalDate startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private User createdBy;

    private String creatorUsername;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

}
