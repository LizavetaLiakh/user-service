package com.innowise.microservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

/**
 * Data Transfer Object for transferring information about users between layers
 * and in REST API responses/requests.
 */
@Data
public class UserDto {

    /**
     * Unique identifier of the user. Must not be NULL.
     */
    @NotNull
    private Long id;

    /**
     * Name of the user. Must start with the capital letter and contain at least 1 symbol.
     */
    @NotBlank
    @Pattern(regexp = "[A-Z][a-z]*")
    private String name;

    /**
     * Surname of the user. Must start with the capital letter and contain at least 1 symbol.
     */
    @NotBlank
    @Pattern(regexp = "[A-Z][a-z]*")
    private String surname;

    /**
     * The date when the user was born. Must be in the past.
     */
    @Past
    private LocalDate birthDate;

    /**
     * Email of the user. Must be a well-formed email address.
     */
    @NotBlank
    @Email
    private String email;
}
