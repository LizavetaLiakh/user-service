package com.innowise.microservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

/**
 * Data Transfer Object for creating or updating a user in REST API requests.
 */
@Data
public class UserRequestDto {

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
