package com.innowise.microservice.dto;

import com.innowise.microservice.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;

/**
 * Data Transfer Object for transferring information about card between layers
 * and in REST API responses/requests.
 */
@Data
public class CardInfoDto {

    /**
     * Unique identifier of the card. Must be not NULL.
     */
    @NotNull
    private Long id;

    /**
     * Unique identifier of the user from table "users". Must be not NULL and must contain at least 1 symbol.
     */
    @NotBlank
    private User userId;

    /**
     * The number of the card. Must contain 16 digits.
     */
    @NotBlank
    @Pattern(regexp = "\\d{16}")
    private String number;

    /**
     * Full name of the card's holder. Must be written with only capital letters and contain name and surname.
     */
    @NotBlank
    @Pattern(regexp = "[A-Z]+\\s[A-Z]+")
    private String holder;

    /**
     * The date when the card expires. Must be not NULL.
     */
    @NotNull
    private LocalDate expirationDate;
}
