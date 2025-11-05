package com.innowise.microservice.mapper;

import com.innowise.microservice.dto.UserRequestDto;
import com.innowise.microservice.dto.UserResponseDto;
import com.innowise.microservice.entity.User;
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
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponseDto toUserResponseDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setId( user.getId() );
        userResponseDto.setName( user.getName() );
        userResponseDto.setSurname( user.getSurname() );
        userResponseDto.setBirthDate( user.getBirthDate() );
        userResponseDto.setEmail( user.getEmail() );

        return userResponseDto;
    }

    @Override
    public List<UserResponseDto> toUserResponseDtoList(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponseDto> list = new ArrayList<UserResponseDto>( users.size() );
        for ( User user : users ) {
            list.add( toUserResponseDto( user ) );
        }

        return list;
    }

    @Override
    public User toUser(UserRequestDto userRequestDto) {
        if ( userRequestDto == null ) {
            return null;
        }

        User user = new User();

        user.setName( userRequestDto.getName() );
        user.setSurname( userRequestDto.getSurname() );
        user.setBirthDate( userRequestDto.getBirthDate() );
        user.setEmail( userRequestDto.getEmail() );

        return user;
    }

    @Override
    public List<User> toUserList(List<UserRequestDto> userRequestDtos) {
        if ( userRequestDtos == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( userRequestDtos.size() );
        for ( UserRequestDto userRequestDto : userRequestDtos ) {
            list.add( toUser( userRequestDto ) );
        }

        return list;
    }
}
