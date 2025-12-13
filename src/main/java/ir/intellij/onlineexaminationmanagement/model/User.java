package ir.intellij.onlineexaminationmanagement.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 5)
    private Long id;
    private String fullName;
    private Integer age;
    private String phoneNumber;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user")
    private UserAccount userAccount;
}
