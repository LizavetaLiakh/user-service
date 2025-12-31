package com.innowise.microservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entity that stores information about users.
 */
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * Unique identifier of the user. Generates automatically by the database.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the user.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Surname of the user.
     */
    @Column(nullable = false)
    private String surname;

    /**
     * The date when the user was born.
     */
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    /**
     * Email of the user.
     */
    @Column(nullable = false, unique = true)
    private String email;
}
