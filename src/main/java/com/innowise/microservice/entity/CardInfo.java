package com.innowise.microservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entity that stores information about cards.
 */
@Entity
@Table(name = "card_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardInfo {

    /**
     * Unique identifier of the card. Generates automatically by the database.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique identifier of the user from table "users". The user who owns the card.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    /**
     * The number of the card.
     */
    @Column(name = "number")
    private String number;

    /**
     * Full name of the card's holder.
     */
    @Column(name = "holder")
    private String holder;

    /**
     * The date when the card expires.
     */
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
}
