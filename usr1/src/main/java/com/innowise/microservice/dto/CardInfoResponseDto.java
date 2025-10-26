package com.innowise.microservice.dto;

import com.innowise.microservice.entity.User;
import lombok.Data;

import java.time.LocalDate;

/**
 * Data Transfer Object for returning user data in REST API responses.
 */
@Data
public class CardInfoResponseDto {

    /**
     * Unique identifier of the card.
     */
    private Long id;

    /**
     * Unique identifier of the user from table "users".
     */
    private User userId;

    /**
     * The number of the card.
     */
    private String number;

    /**
     * Full name of the card's holder.
     */
    private String holder;

    /**
     * The date when the card expires.
     */
    private LocalDate expirationDate;
}
