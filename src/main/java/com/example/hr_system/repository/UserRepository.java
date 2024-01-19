package com.example.hr_system.repository;

import com.example.hr_system.entities.User;
import com.example.hr_system.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role IN : roles ")
    List<User> findAllByRoleAndRole(@Param("roles") List<Role> roles);

    List<User> findAllByRole(Role role);
    @Query("SELECT u FROM User u WHERE u.role IN :roles")
    List<User> findEmployersAndJobSeekers(@Param("roles") List<Role> roles);



    User findByEmailAndPassword(String email, String password);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN u.jobSeeker js " +
            "LEFT JOIN u.employer emp " +
            "WHERE lower(js.firstname) LIKE lower(concat('%', :name, '%')) " +
            "OR lower(js.lastname) LIKE lower(concat('%', :name, '%')) " +
            "OR lower(emp.companyName) LIKE lower(concat('%', :name, '%'))")
    List<User> searchByFirstNameLastNameAndCompanyName(@Param("name") String name);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN u.jobSeeker js " +
            "LEFT JOIN u.employer emp " +
            "WHERE (lower(js.firstname) LIKE lower(concat('%', :name, '%')) " +
            "OR lower(js.lastname) LIKE lower(concat('%', :name, '%')) " +
            "OR lower(emp.companyName) LIKE lower(concat('%', :name, '%'))) " +
            "AND u.role = :role " +
            "ORDER BY u.firstname ASC, u.role ASC")
    List<User> searchByNameAndRoleSortedByNameAndRole(
            @Param("name") String name,
            @Param("role") Role role
    );


    Optional<User> findByVerificationCode(String code);
}
