package com.innowise.microservice.service;

import com.innowise.microservice.dto.CardInfoRequestDto;
import com.innowise.microservice.dto.CardInfoResponseDto;
import com.innowise.microservice.entity.CardInfo;
import com.innowise.microservice.entity.User;
import com.innowise.microservice.exception.CardNotFoundException;
import com.innowise.microservice.exception.CardNumberExistsException;
import com.innowise.microservice.exception.EmptyCardListException;
import com.innowise.microservice.mapper.CardInfoMapper;
import com.innowise.microservice.repository.CardInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardInfoServiceTest {

    @Mock
    private CardInfoMapper mapper;

    @Mock
    private CardInfoRepository repository;

    @InjectMocks
    private CardInfoService service;

    private User user;
    private User user2;
    private CardInfo card;
    private CardInfo card2;
    private CardInfoRequestDto requestCardDto;
    private CardInfoResponseDto responseCardDto;
    private CardInfoResponseDto responseCardDto2;

    private final static int CARD_LIST_SIZE = 2;

    @BeforeEach
    void setUpCards() {
        MockitoAnnotations.openMocks(this);

        user = new User(1L, "Hanna", "Montana", LocalDate.of(2000, 3, 20)
                , "hanna00@gmail.com");
        card = new CardInfo(1L, user, "1234123412341234", "Hanna Montana",
                LocalDate.of(2027, 11, 10));

        requestCardDto = new CardInfoRequestDto();
        requestCardDto.setUserId(user);
        requestCardDto.setNumber("1234123412341234");
        requestCardDto.setHolder("Hanna Montana");
        requestCardDto.setExpirationDate(LocalDate.of(2027, 11, 10));

        responseCardDto = new CardInfoResponseDto();
        responseCardDto.setId(1L);
        responseCardDto.setUserId(user);
        responseCardDto.setNumber("1234123412341234");
        responseCardDto.setHolder("Hanna Montana");
        responseCardDto.setExpirationDate(LocalDate.of(2027, 11, 10));

        user2 = new User(2L, "Patrick", "Wong", LocalDate.of(1995, 5, 15)
                , "pat@gmail.com");

        card2 = new CardInfo(2L, user2, "4444888844449999", "Patrick Wong",
                LocalDate.of(2027, 12, 9));

        responseCardDto2 = new CardInfoResponseDto();
        responseCardDto2.setId(2L);
        responseCardDto2.setUserId(user2);
        responseCardDto2.setNumber("4444888844449999");
        responseCardDto2.setHolder("Patrick Wong");
        responseCardDto2.setExpirationDate(LocalDate.of(2027, 12, 9));
    }

    @Test
    void testCreateCard() {
        when(repository.findByNumber(requestCardDto.getNumber())).thenReturn(Optional.empty());
        when(mapper.toCardInfo(requestCardDto)).thenReturn(card);
        when(repository.save(card)).thenReturn(card);
        when(mapper.toCardInfoResponseDto(card)).thenReturn(responseCardDto);

        CardInfoResponseDto resultResponseCardDto = service.createCard(requestCardDto);

        assertNotNull(resultResponseCardDto);
        assertEquals(responseCardDto.getId(), resultResponseCardDto.getId());
        assertEquals(responseCardDto.getNumber(), resultResponseCardDto.getNumber());
        verify(repository).findByNumber(requestCardDto.getNumber());
        verify(repository).save(card);
        verify(mapper).toCardInfo(requestCardDto);
        verify(mapper).toCardInfoResponseDto(card);
    }

    @Test
    void testCreateCardNumberExists() {
        when(repository.findByNumber(requestCardDto.getNumber())).thenReturn(Optional.of(card));

        assertThrows(CardNumberExistsException.class, () -> service.createCard(requestCardDto));
        verify(repository).findByNumber(requestCardDto.getNumber());
        verify(repository, never()).save(any());
        verify(mapper, never()).toCardInfo(any());
        verify(mapper, never()).toCardInfoResponseDto(any());
    }

    @Test
    void testGetCardById() {
        when(repository.findById(1L)).thenReturn(Optional.of(card));
        when(mapper.toCardInfoResponseDto(card)).thenReturn(responseCardDto);

        CardInfoResponseDto resultResponseCardDto = service.getCardById(1L);

        assertNotNull(resultResponseCardDto);
        assertEquals(responseCardDto.getId(), resultResponseCardDto.getId());
        assertEquals(responseCardDto.getNumber(), resultResponseCardDto.getNumber());
        assertEquals(responseCardDto.getHolder(), resultResponseCardDto.getHolder());
        assertEquals(responseCardDto.getExpirationDate(), resultResponseCardDto.getExpirationDate());
        verify(repository).findById(1L);
        verify(mapper).toCardInfoResponseDto(card);
    }

    @Test
    void testGetCardByIdNotFound() {
        when(repository.findById(100L)).thenReturn(Optional.empty());

        CardNotFoundException ex = assertThrows(CardNotFoundException.class, () -> service.getCardById(100L));
        assertEquals("Card with id 100 not found", ex.getMessage());
        verify(repository).findById(100L);
        verify(mapper, never()).toCardInfoResponseDto(any());
    }

    @Test
    void testGetCardsByIds() {
        List<Long> ids = List.of(1L, 2L);
        List<CardInfo> cards = List.of(card, card2);

        when(repository.findAllById(ids)).thenReturn(cards);
        when(mapper.toCardInfoResponseDto(card)).thenReturn(responseCardDto);
        when(mapper.toCardInfoResponseDto(card2)).thenReturn(responseCardDto2);

        List<CardInfoResponseDto> resultList = service.getCardsByIds(ids);

        assertNotNull(resultList);
        assertEquals(CARD_LIST_SIZE, resultList.size());
        assertEquals("Hanna Montana", resultList.get(0).getHolder());
        assertEquals("Patrick Wong", resultList.get(1).getHolder());
        verify(repository).findAllById(ids);
        verify(mapper).toCardInfoResponseDto(card);
        verify(mapper).toCardInfoResponseDto(card2);
    }

    @Test
    void testGetCardsByIdsEmpty() {
        List<Long> ids = List.of(100L, 101L);

        when(repository.findAllById(ids)).thenReturn(new ArrayList<>());
        EmptyCardListException ex = assertThrows(EmptyCardListException.class, () -> service.getCardsByIds(ids));
        assertEquals("No cards found with ids: [100, 101]", ex.getMessage());
        verify(repository).findAllById(ids);
        verify(mapper, never()).toCardInfoResponseDto(any());
    }

    @Test
    void testUpdateCardById() {
        requestCardDto.setUserId(user2);
        requestCardDto.setNumber("4444888844449999");
        requestCardDto.setHolder("Patrick Wong");
        requestCardDto.setExpirationDate(LocalDate.of(2027, 12, 9));

        when(repository.findByNumber(requestCardDto.getNumber())).thenReturn(Optional.empty());
        when(repository.updateCardInfo(eq(1L), any(), anyString(), anyString(), any())).thenReturn(1);
        when(repository.findById(1L)).thenReturn(Optional.of(card2));
        when(mapper.toCardInfoResponseDto(card2)).thenReturn(responseCardDto2);

        CardInfoResponseDto resultCardResponseDto = service.updateCardById(1L, requestCardDto);

        assertNotNull(resultCardResponseDto);
        assertEquals(user2, resultCardResponseDto.getUserId());
        assertEquals("4444888844449999", resultCardResponseDto.getNumber());
        assertEquals("Patrick Wong", resultCardResponseDto.getHolder());
        assertEquals(LocalDate.of(2027, 12, 9), resultCardResponseDto.getExpirationDate());

        verify(repository).findByNumber("4444888844449999");
        verify(repository).updateCardInfo(1L, user2.getId(), "4444888844449999", "Patrick Wong",
                LocalDate.of(2027, 12, 9));
        verify(repository).findById(1L);
        verify(mapper).toCardInfoResponseDto(card2);
    }

    @Test
    void testUpdateCardNumberExists() {
        requestCardDto.setUserId(user2);
        requestCardDto.setNumber("4444888844449999");
        requestCardDto.setHolder("Patrick Bom");
        requestCardDto.setExpirationDate(LocalDate.of(2027, 11, 10));

        CardInfo sameNumberCard = new CardInfo(3L, user2, "4444888844449999", "Patrick Bom",
                LocalDate.of(2028, 3, 25));

        when(repository.findByNumber(requestCardDto.getNumber())).thenReturn(Optional.of(sameNumberCard));

        CardNumberExistsException ex = assertThrows(CardNumberExistsException.class,
                () -> service.updateCardById(2L, requestCardDto));
        assertEquals("Card with number 4444888844449999 already exists", ex.getMessage());
        verify(repository).findByNumber("4444888844449999");
        verify(repository, never()).updateCardInfo(anyLong(), any(), anyString(), anyString(), any());
    }

    @Test
    void testUpdateCardInfoNotFound() {
        requestCardDto.setUserId(user2);
        requestCardDto.setNumber("4444888844448888");
        requestCardDto.setHolder("Patrick Bom");
        requestCardDto.setExpirationDate(LocalDate.of(2028, 3, 25));

        when(repository.findByNumber(requestCardDto.getNumber())).thenReturn(Optional.empty());
        when(repository.updateCardInfo(eq(2L), eq(requestCardDto.getUserId().getId()),
                eq(requestCardDto.getNumber()), eq(requestCardDto.getHolder()),
                eq(requestCardDto.getExpirationDate()))).thenReturn(1);
        when(repository.findById(2L)).thenReturn(Optional.empty());

        CardNotFoundException ex = assertThrows(CardNotFoundException.class,
                () -> service.updateCardById(2L, requestCardDto));
        assertEquals("Card with id 2 not found", ex.getMessage());
        verify(repository).findByNumber("4444888844448888");
        verify(repository).updateCardInfo(2L, user2.getId(), "4444888844448888", "Patrick Bom",
                LocalDate.of(2028, 3, 25));
        verify(repository).findById(2L);
        verify(mapper, never()).toCardInfoResponseDto(any());
    }

    @Test
    void testDeleteCardById() {
        assertDoesNotThrow(() -> service.deleteCardById(1L));
        verify(repository).deleteById(1L);
    }

    @Test
    void testDeleteCardByIdNotFound() {
        doThrow(new org.springframework.dao.EmptyResultDataAccessException(1)).when(repository).deleteById(100L);
        CardNotFoundException ex = assertThrows(CardNotFoundException.class, () -> service.deleteCardById(100L));
        assertEquals("Card with id 100 not found", ex.getMessage());
        verify(repository).deleteById(100L);
    }
}
