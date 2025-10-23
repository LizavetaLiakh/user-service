package com.innowise.microservice.controller;

import com.innowise.microservice.dto.CardInfoDto;
import com.innowise.microservice.service.CardInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * REST-controller for card management.
 * <p>
 * Provides CRUD-operations:
 * <ul>
 *     <li>Creating a new card</li>
 *     <li>Getting a card by id</li>
 *     <li>Getting a list of cards by their ids</li>
 *     <li>Updating a card by id</li>
 *     <li>Deleting a card by id</li>
 * </ul>
 */
@RestController
@RequestMapping("/cards")
public class CardInfoController {

    private final CardInfoService service;

    public CardInfoController(CardInfoService service) {
        this.service = service;
    }

    /**
     * Creates a new user.
     *
     * @param cardInfoDto New card's data.
     * @return Created card.
     * @throws com.innowise.microservice.exception.CardNumberExistsException If given number is already registered for
     * another card.
     * @response 201 Created - New card successfully created.
     * @response 500 Internal Server Error - Unexpected server error occurred.
     */
    @PostMapping("/add")
    public ResponseEntity<CardInfoDto> addCard(@RequestBody CardInfoDto cardInfoDto) {
        CardInfoDto newCard = service.createCard(cardInfoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCard);
    }

    /**
     * Finds a card by id.
     *
     * @param id Card's id.
     * @return Found card.
     * @throws com.innowise.microservice.exception.CardNotFoundException If there's no card with given id.
     * @response 200 OK - Card found.
     * @response 404 Not Found - Card not found.
     * @response 500 Internal Server Error - Unexpected server error occurred.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<CardInfoDto> getCardById(@PathVariable Long id) {
        CardInfoDto card = service.getCardById(id);
        return ResponseEntity.status(HttpStatus.OK).body(card);
    }

    /**
     * Finds cards by their ids.
     *
     * @param ids A list of cards' ids.
     * @return A list of found cards.
     * @throws com.innowise.microservice.exception.EmptyCardListException If there's no cards with given ids.
     * @response 200 OK - Cards found.
     * @response 404 Not Found - Cards not found.
     * @response 500 Internal Server Error - Unexpected server error occurred.
     */
    @GetMapping("/get")
    public ResponseEntity<List<CardInfoDto>> getCardsByIds(@RequestParam List<Long> ids) {
        List<CardInfoDto> cards = service.getCardsByIds(ids);
        return ResponseEntity.status(HttpStatus.OK).body(cards);
    }

    /**
     * Updates a card with given id.
     *
     * @param id Identifier of the card that should be updated.
     * @param cardInfoDto New data of the card.
     * @throws com.innowise.microservice.exception.CardNotFoundException If there's no card with given id.
     * @throws com.innowise.microservice.exception.CardNumberExistsException If given number is already registered for
     * another card.
     * @response 200 OK - Card successfully updated.
     * @response 404 Not Found - Card not found.
     * @response 500 Internal Server Error - Unexpected server error occurred.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateCard(@PathVariable Long id, @RequestBody CardInfoDto cardInfoDto) {
        service.updateCardById(id, cardInfoDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Deletes a card with given id.
     *
     * @param id Identifier of the card that should be deleted.
     * @throws com.innowise.microservice.exception.CardNotFoundException If there's no card with given id.
     * @response 204 No Content - Card successfully deleted.
     * @response 404 Not Found - Card not found.
     * @response 500 Internal Server Error - Unexpected server error occurred.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        service.deleteCardById(id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
