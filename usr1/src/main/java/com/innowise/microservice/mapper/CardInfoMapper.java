package com.innowise.microservice.mapper;

import com.innowise.microservice.dto.CardInfoDto;
import com.innowise.microservice.entity.CardInfo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import java.util.List;

/**
 * Mapper for converting CardInfoDto to CardInfo and CardInfo to CardInfoDto.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardInfoMapper {

    /**
     * Maps CardInfo to CardInfoDto.
     * @param cardInfo entity object that needs to be mapped
     * @return CardInfoDto object
     */
    CardInfoDto toCardInfoDto(CardInfo cardInfo);

    /**
     * Maps list of CardInfos to list of CardInfoDto objects.
     * @param cardInfos list of entity objects that need to be mapped
     * @return list of CardInfoDto objects
     */
    List<CardInfoDto> toCardInfoDtoList(List<CardInfo> cardInfos);

    /**
     * Maps CardInfoDto to CardInfo.
     * @param cardInfoDto DTO object that needs to be mapped
     * @return CardInfo object
     */
    CardInfo toCardInfo(CardInfoDto cardInfoDto);

    /**
     * Maps list of CardInfoDtos to list of CardInfo objects.
     * @param cardInfoDtos list of DTO objects that need to be mapped
     * @return list of CardInfo objects
     */
    List<CardInfo> toCardInfoList(List<CardInfoDto> cardInfoDtos);
}
