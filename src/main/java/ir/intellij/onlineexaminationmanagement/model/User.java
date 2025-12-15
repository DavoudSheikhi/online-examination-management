package ir.intellij.onlineexaminationmanagement.model;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 5)
    private Long id;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
}
