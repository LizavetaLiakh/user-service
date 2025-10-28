package com.innowise.microservice.integration;

import com.innowise.microservice.dto.CardInfoRequestDto;
import com.innowise.microservice.dto.CardInfoResponseDto;
import com.innowise.microservice.entity.User;
import com.innowise.microservice.exception.CardNotFoundException;
import com.innowise.microservice.exception.CardNumberExistsException;
import com.innowise.microservice.exception.EmptyCardListException;
import com.innowise.microservice.repository.CardInfoRepository;
import com.innowise.microservice.repository.UserRepository;
import com.innowise.microservice.service.CardInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardInfoServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private CardInfoService service;

    @Autowired
    private CardInfoRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreateAndGetCard() {
        User user = new User(null, "Hanna", "Montana", LocalDate.of(2000, 3, 20)
                , "hanna00@gmail.com");
        user = userRepository.save(user);

        CardInfoRequestDto cardInfoRequestDto = new CardInfoRequestDto();
        cardInfoRequestDto.setUserId(user);
        cardInfoRequestDto.setNumber("1234123412341234");
        cardInfoRequestDto.setHolder("Hanna Montana");
        cardInfoRequestDto.setExpirationDate(LocalDate.of(2027, 11, 10));

        CardInfoResponseDto createdCard = service.createCard(cardInfoRequestDto);

        assertNotNull(createdCard);
        assertEquals(user.getId(), createdCard.getUserId().getId());
        assertEquals("1234123412341234", createdCard.getNumber());
        assertEquals("Hanna Montana", createdCard.getHolder());
        assertEquals(LocalDate.of(2027, 11, 10), createdCard.getExpirationDate());

        CardInfoResponseDto foundCard = service.getCardById(createdCard.getId());

        assertEquals(createdCard.getUserId().getId(), foundCard.getUserId().getId());
        assertEquals(createdCard.getNumber(), foundCard.getNumber());
        assertEquals(createdCard.getHolder(), foundCard.getHolder());
        assertEquals(createdCard.getExpirationDate(), foundCard.getExpirationDate());
    }

    @Test
    void testGetCardByNonExistingId() {
        Long nonExistingId = 105L;
        assertThrows(CardNotFoundException.class, () -> service.getCardById(nonExistingId));
    }

    @Test
    void testUpdateCardById() {
        User user = new User(null, "Patrick", "Wong", LocalDate.of(1995, 5, 15)
                , "pat@gmail.com");
        user = userRepository.save(user);

        CardInfoRequestDto cardInfoRequestDto = new CardInfoRequestDto();
        cardInfoRequestDto.setUserId(user);
        cardInfoRequestDto.setNumber("4444888844449999");
        cardInfoRequestDto.setHolder("Patrick Wong");
        cardInfoRequestDto.setExpirationDate(LocalDate.of(2027, 12, 9));

        CardInfoResponseDto createdCard = service.createCard(cardInfoRequestDto);
        assertNotNull(createdCard);

        CardInfoRequestDto updatedCard = new CardInfoRequestDto();
        updatedCard.setUserId(user);
        updatedCard.setNumber("4444888844448888");
        updatedCard.setHolder("Patrick Wang");
        updatedCard.setExpirationDate(LocalDate.of(2027, 12, 8));

        CardInfoResponseDto updatedResponseCard = service.updateCardById(createdCard.getId(), updatedCard);

        assertNotNull(updatedResponseCard);
        assertEquals(createdCard.getId(), updatedResponseCard.getId());
        assertEquals(user.getId(), updatedResponseCard.getUserId().getId());
        assertEquals("4444888844448888", updatedResponseCard.getNumber());
        assertEquals("Patrick Wang", updatedResponseCard.getHolder());
        assertEquals(LocalDate.of(2027, 12, 8), updatedResponseCard.getExpirationDate());
    }

    @Test
    void testGetCardsByIdsEmpty() {
        List<Long> ids = List.of(100L, 101L);
        assertThrows(EmptyCardListException.class, () -> service.getCardsByIds(ids));
    }

    @Test
    void testCreateCardNumberExists() {
        User user = new User(null, "Tom", "Young", LocalDate.of(1995, 2, 2),
                "tom@gmail.com");
        user = userRepository.save(user);

        CardInfoRequestDto cardInfoRequestDto = new CardInfoRequestDto();
        cardInfoRequestDto.setUserId(user);
        cardInfoRequestDto.setNumber("1111222233334444");
        cardInfoRequestDto.setHolder("Tom Young");
        cardInfoRequestDto.setExpirationDate(LocalDate.of(2027, 10, 1));
        service.createCard(cardInfoRequestDto);

        CardInfoRequestDto cardInfoRequestDto2 = new CardInfoRequestDto();
        cardInfoRequestDto2.setUserId(user);
        cardInfoRequestDto2.setNumber("1111222233334444");
        cardInfoRequestDto2.setHolder("Tom Young");
        cardInfoRequestDto2.setExpirationDate(LocalDate.of(2027, 10, 2));

        assertThrows(CardNumberExistsException.class, () -> service.createCard(cardInfoRequestDto2));
    }

    @Test
    void testDeleteCardById() {
        User user = new User(null, "Mary", "Alice", LocalDate.of(2003, 1, 22), 
                "alicemary@gmail.com");
        user = userRepository.save(user);

        CardInfoRequestDto cardInfoRequestDto = new CardInfoRequestDto();
        cardInfoRequestDto.setUserId(user);
        cardInfoRequestDto.setNumber("5588100099994422");
        cardInfoRequestDto.setHolder("Mary Alice");
        cardInfoRequestDto.setExpirationDate(LocalDate.of(2028, 1, 11));

        CardInfoResponseDto createdCard = service.createCard(cardInfoRequestDto);
        assertNotNull(createdCard);

        service.deleteCardById(createdCard.getId());

        assertThrows(CardNotFoundException.class, () -> service.getCardById(createdCard.getId()));
    }
}
