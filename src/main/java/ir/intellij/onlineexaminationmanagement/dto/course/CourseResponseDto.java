package ir.intellij.onlineexaminationmanagement.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class CourseResponseDto {
    private String title;
    private String courseCode;
    private LocalDate startDate;
    private LocalDate endDate;
}
