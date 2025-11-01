package com.innowise.microservice.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * Data Transfer Object for returning user data in REST API responses.
 */
@Data
public class UserResponseDto {

    /**
     * Unique identifier of the user.
     */
    private Long id;

    /**
     * Name of the user.
     */
    private String name;

    /**
     * Surname of the user.
     */
    private String surname;

    /**
     * The date when the user was born.
     */
    private LocalDate birthDate;

    /**
     * Email of the user.
     */
    private String email;
}
