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
    @Column(name = "name")
    private String name;

    /**
     * Surname of the user.
     */
    @Column(name = "surname")
    private String surname;

    /**
     * The date when the user was born.
     */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * Email of the user.
     */
    @Column(name = "email")
    private String email;
}
