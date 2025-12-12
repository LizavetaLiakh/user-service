package com.innowise.microservice.dto;

import com.innowise.microservice.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

/**
 * Data Transfer Object for creating or updating a card in REST API requests.
 */
@Data
public class CardInfoRequestDto {

    /**
     * Unique identifier of the user from table "users". Must be not NULL and must contain at least 1 symbol.
     */
    @NotNull(message = "User ID must not be NULL")
    private Long userId;

    /**
     * The number of the card. Must contain 16 digits.
     */
    @Pattern(regexp = "\\d{16}", message = "Card number must contain exactly 16 digits")
    private String number;

    /**
     * Full name of the card's holder. Must be written with only capital letters and contain name and surname.
     */
    @Pattern(regexp = "[A-Z]+\\s[A-Z]+", message = "Holder name must be in format 'NAME SURNAME'")
    private String holder;

    /**
     * The date when the card expires. Must be not NULL.
     */
    @NotNull(message = "Expiration date must not be NULL")
    private LocalDate expirationDate;
}
