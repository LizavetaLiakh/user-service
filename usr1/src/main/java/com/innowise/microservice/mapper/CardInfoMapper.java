package com.innowise.microservice.mapper;

import com.innowise.microservice.dto.CardInfoRequestDto;
import com.innowise.microservice.dto.CardInfoResponseDto;
import com.innowise.microservice.entity.CardInfo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import java.util.List;

/**
 * Mapper for converting between CardInfo entity, CardInfoRequestDto and CardInfoResponseDto.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardInfoMapper {

    /**
     * Maps CardInfo entity entity to CardInfoResponseDto.
     * @param cardInfo entity object that needs to be mapped
     * @return CardInfoResponseDto object
     */
    CardInfoResponseDto toCardInfoResponseDto(CardInfo cardInfo);

    /**
     * Maps list of CardInfos to list of CardInfoResponseDto objects.
     * @param cardInfos list of entity objects that need to be mapped
     * @return list of CardInfoResponseDto objects
     */
    List<CardInfoResponseDto> toCardInfoResponseDtoList(List<CardInfo> cardInfos);

    /**
     * Maps CardInfoRequestDto to CardInfo entity.
     * @param cardInfoRequestDto DTO object that needs to be mapped
     * @return CardInfo entity
     */
    CardInfo toCardInfo(CardInfoRequestDto cardInfoRequestDto);

    /**
     * Maps list of CardInfoRequestDto to list of CardInfo entities.
     * @param cardInfoRequestDtos list of DTO objects that need to be mapped
     * @return list of CardInfo objects
     */
    List<CardInfo> toCardInfoList(List<CardInfoRequestDto> cardInfoRequestDtos);
}
