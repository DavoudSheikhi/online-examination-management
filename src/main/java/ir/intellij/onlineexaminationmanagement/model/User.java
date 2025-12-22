package ir.intellij.onlineexaminationmanagement.model;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "users")
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
//@DiscriminatorValue(value = "USER")
@Getter
@Setter
@NoArgsConstructor
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

    public User(String fullName, String phoneNumber, Integer age, String username, String password, Role role, UserStatus userStatus) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.username = username;
        this.password = password;
        this.role = role;
        this.userStatus = userStatus;
    }
}

