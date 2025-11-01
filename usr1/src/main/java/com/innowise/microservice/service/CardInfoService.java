package com.innowise.microservice.service;

import com.innowise.microservice.dto.CardInfoRequestDto;
import com.innowise.microservice.dto.CardInfoResponseDto;
import com.innowise.microservice.entity.CardInfo;
import com.innowise.microservice.exception.CardNotFoundException;
import com.innowise.microservice.exception.CardNumberExistsException;
import com.innowise.microservice.exception.EmptyCardListException;
import com.innowise.microservice.mapper.CardInfoMapper;
import com.innowise.microservice.repository.CardInfoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing CardInfo entity.
 * <p>
 *     Provides CRUD operations: create, get card by id, get cards by ids, get card by email, update card by id,
 *     delete card by id.
 * </p>
 */
@Service
public class CardInfoService {

    private final CardInfoRepository repository;
    private final CardInfoMapper mapper;

    public CardInfoService(CardInfoRepository repository, CardInfoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Creates a new card in the database.
     * @param cardDto DTO with new card's data
     * @return created card as DTO
     */
    @CachePut(value = "CARD_CACHE", key = "#result.id()")
    public CardInfoResponseDto createCard(CardInfoRequestDto cardDto) {
        repository.findByNumber(cardDto.getNumber())
                .ifPresent(sameNumberCard -> {
                    throw new CardNumberExistsException(cardDto.getNumber());
                });
        CardInfo card = mapper.toCardInfo(cardDto);
        CardInfo savedCard = repository.save(card);
        return mapper.toCardInfoResponseDto(savedCard);
    }

    /**
     * Finds a card by id.
     * @param id card's unique identifier
     * @return card as DTO if found, empty if not found
     */
    @Cacheable(value = "CARD_CACHE", key = "#id")
    public CardInfoResponseDto getCardById(Long id) {
        return repository.findById(id)
                .map(mapper::toCardInfoResponseDto)
                .orElseThrow(() -> new CardNotFoundException(id));
    }

    /**
     * Finds cards by ids.
     * @param ids list of cards' unique identifiers
     * @return list of cards as DTOs
     */
    public List<CardInfoResponseDto> getCardsByIds(Iterable<Long> ids) {
        List<CardInfoResponseDto> cards = repository.findAllById(ids)
                .stream()
                .map(mapper::toCardInfoResponseDto)
                .toList();
        if (cards.isEmpty()) {
            throw new EmptyCardListException(ids);
        }
        return cards;
    }

    /**
     * Updates a card by id.
     * @param id card's unique identifier
     * @param newCardDto CardInfoDto that contains current data
     */
    @CachePut(value = "CARD_CACHE", key = "#id")
    @Transactional
    public CardInfoResponseDto updateCardById(Long id, CardInfoRequestDto newCardDto) {
        repository.findByNumber(newCardDto.getNumber())
                .ifPresent(sameNumberCard -> {
                    if (!sameNumberCard.getId().equals(id)) {
                        throw new CardNumberExistsException(newCardDto.getNumber());
                    }
                });
        int updated = repository.updateCardInfo(id, newCardDto.getUserId(), newCardDto.getNumber(), newCardDto.getHolder(),
                newCardDto.getExpirationDate());
        if (updated == 0) {
            throw new CardNotFoundException(id);
        }
        return repository.findById(id)
                .map(mapper::toCardInfoResponseDto)
                .orElseThrow(() -> new CardNotFoundException(id));
    }

    /**
     * Deletes a card by id.
     * @param id card's id
     */
    @CacheEvict(value = "CARD_CACHE", key = "#id")
    @Transactional
    public void deleteCardById(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new CardNotFoundException(id);
        }
    }
}