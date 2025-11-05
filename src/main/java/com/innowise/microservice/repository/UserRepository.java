package com.innowise.microservice.repository;

import com.innowise.microservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Query(value = "UPDATE users SET name = :name, surname = :surname, birth_date = :birthDate, email = :email " +
            "WHERE id = :id", nativeQuery = true)
    int updateUser(@Param("id") Long id, @Param("name") String name, @Param("surname") String surname
            , @Param("birthDate") LocalDate birthDate, @Param("email") String email);
}
