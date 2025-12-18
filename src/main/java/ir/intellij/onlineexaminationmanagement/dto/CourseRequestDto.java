package ir.intellij.onlineexaminationmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class CourseRequestDto {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

}
