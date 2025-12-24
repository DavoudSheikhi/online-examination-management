package ir.intellij.onlineexaminationmanagement.repository;

import ir.intellij.onlineexaminationmanagement.model.Course;
import ir.intellij.onlineexaminationmanagement.model.Role;
import ir.intellij.onlineexaminationmanagement.model.User;
import ir.intellij.onlineexaminationmanagement.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUserByRole(Role role);

    User findByUsername(String username);

    List<User> findUsersByUserStatus(UserStatus userStatus);

    @Query("""
               SELECT u FROM User u
               WHERE (:fullName IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT(:fullName, '%')))
                 AND (:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')))
                 AND (:phone IS NULL OR u.phoneNumber LIKE CONCAT(:phone, '%'))
                 AND (:age IS NULL OR u.age = :age)
                 AND (:role IS NULL OR u.role = :role)
                 AND (:status IS NULL OR u.userStatus = :status)
            """)
    List<User> searchUsers(
            @Param("fullName") String fullName,
            @Param("username") String username,
            @Param("phone") String phone,
            @Param("age") Integer age,
            @Param("role") Role role,
            @Param("status") UserStatus status
    );

    List<User> findUsersByRoleAndUserStatus(Role role, UserStatus status);

    @Query("""
            select u
            from User u
            where u.role = :role
              and u.userStatus = :status
              and u not in (
                  select e
                  from Course c
                  join c.enrolledStudents e
                  where c.courseCode = :courseCode
              )
            """)
    List<User> findEligibleStudents(
            @Param("courseCode") String courseCode,
            @Param("role") Role role,
            @Param("status") UserStatus status
    );

    @Query("""
            select s
            from Course c
            join c.enrolledStudents s
            where c.courseCode = :courseCode
            """)
    List<User> findStudentsByCourseCode(
            @Param("courseCode") String courseCode);

    @Query("""
            select c.teacher
            from Course c
            where c.courseCode = :courseCode
            """)
    User findTeacherByCourseCode(
            @Param("courseCode") String courseCode);

}
