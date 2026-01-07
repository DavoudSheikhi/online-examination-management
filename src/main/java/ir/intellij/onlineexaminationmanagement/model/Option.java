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
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "option_seq")
    @SequenceGenerator(name = "option_seq", sequenceName = "option_seq", allocationSize = 5)
    private Long id;

    @NotBlank
    private String text;
}
