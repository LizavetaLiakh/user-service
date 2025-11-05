package com.innowise.microservice.mapper;

import com.innowise.microservice.dto.CardInfoRequestDto;
import com.innowise.microservice.dto.CardInfoResponseDto;
import com.innowise.microservice.entity.CardInfo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-30T13:27:37+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class CardInfoMapperImpl implements CardInfoMapper {

    @Override
    public CardInfoResponseDto toCardInfoResponseDto(CardInfo cardInfo) {
        if ( cardInfo == null ) {
            return null;
        }

        CardInfoResponseDto cardInfoResponseDto = new CardInfoResponseDto();

        cardInfoResponseDto.setId( cardInfo.getId() );
        cardInfoResponseDto.setUserId( cardInfo.getUserId() );
        cardInfoResponseDto.setNumber( cardInfo.getNumber() );
        cardInfoResponseDto.setHolder( cardInfo.getHolder() );
        cardInfoResponseDto.setExpirationDate( cardInfo.getExpirationDate() );

        return cardInfoResponseDto;
    }

    @Override
    public List<CardInfoResponseDto> toCardInfoResponseDtoList(List<CardInfo> cardInfos) {
        if ( cardInfos == null ) {
            return null;
        }

        List<CardInfoResponseDto> list = new ArrayList<CardInfoResponseDto>( cardInfos.size() );
        for ( CardInfo cardInfo : cardInfos ) {
            list.add( toCardInfoResponseDto( cardInfo ) );
        }

        return list;
    }

    @Override
    public CardInfo toCardInfo(CardInfoRequestDto cardInfoRequestDto) {
        if ( cardInfoRequestDto == null ) {
            return null;
        }

        CardInfo cardInfo = new CardInfo();

        cardInfo.setUserId( cardInfoRequestDto.getUserId() );
        cardInfo.setNumber( cardInfoRequestDto.getNumber() );
        cardInfo.setHolder( cardInfoRequestDto.getHolder() );
        cardInfo.setExpirationDate( cardInfoRequestDto.getExpirationDate() );

        return cardInfo;
    }

    @Override
    public List<CardInfo> toCardInfoList(List<CardInfoRequestDto> cardInfoRequestDtos) {
        if ( cardInfoRequestDtos == null ) {
            return null;
        }

        List<CardInfo> list = new ArrayList<CardInfo>( cardInfoRequestDtos.size() );
        for ( CardInfoRequestDto cardInfoRequestDto : cardInfoRequestDtos ) {
            list.add( toCardInfo( cardInfoRequestDto ) );
        }

        return list;
    }
}
