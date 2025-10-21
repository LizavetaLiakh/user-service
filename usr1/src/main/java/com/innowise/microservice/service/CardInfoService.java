package com.innowise.microservice.service;

import com.innowise.microservice.dto.CardInfoDto;
import com.innowise.microservice.dto.UserDto;
import com.innowise.microservice.entity.CardInfo;
import com.innowise.microservice.entity.User;
import com.innowise.microservice.mapper.CardInfoMapper;
import com.innowise.microservice.mapper.UserMapper;
import com.innowise.microservice.repository.CardInfoRepository;
import com.innowise.microservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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
    public CardInfoDto createCard(CardInfoDto cardDto) {
        CardInfo card = mapper.toCardInfo(cardDto);
        CardInfo savedCard = repository.save(card);
        return mapper.toCardInfoDto(savedCard);
    }

    /**
     * Finds a card by id.
     * @param id card's unique identifier
     * @return card as DTO if found, empty if not found
     */
    public Optional<CardInfoDto> getCardById(Long id) {
        return repository.findById(id)
                .map(mapper::toCardInfoDto);
    }

    /**
     * Finds cards by ids.
     * @param ids list of cards' unique identifiers
     * @return list of cards as DTOs
     */
    public List<CardInfoDto> getCardsByIds(Iterable<Long> ids) {
        return repository.findAllById(ids)
                .stream()
                .map(mapper::toCardInfoDto)
                .toList();
    }

    /**
     * Updates a card by id.
     * @param id card's unique identifier
     * @param newCardDto CardInfoDto that contains current data
     * @return number of updated rows, 0 if user was not found
     */
    @Transactional
    public int updateCardById(Long id, CardInfoDto newCardDto) {
        return repository.updateCardInfo(id, newCardDto.getUserId(), newCardDto.getNumber(), newCardDto.getHolder(),
                newCardDto.getExpirationDate());
    }

    /**
     * Deletes a card by id.
     * @param id card's id
     */
    @Transactional
    public void deleteCardById(Long id) {
        repository.deleteById(id);
    }
}