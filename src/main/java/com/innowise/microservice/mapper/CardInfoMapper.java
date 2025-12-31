package com.innowise.microservice.mapper;

import com.innowise.microservice.dto.CardInfoRequestDto;
import com.innowise.microservice.dto.CardInfoResponseDto;
import com.innowise.microservice.entity.CardInfo;
import com.innowise.microservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
    @Mapping(target = "userId", source = "user.id")
    CardInfoResponseDto toCardInfoResponseDto(CardInfo cardInfo);

    /**
     * Maps CardInfoRequestDto to CardInfo entity.
     * @param cardInfoRequestDto DTO object that needs to be mapped
     * @return CardInfo entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", expression = "java(toUser(cardInfoRequestDto.getUserId()))")
    CardInfo toCardInfo(CardInfoRequestDto cardInfoRequestDto);

    default User toUser(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
}
