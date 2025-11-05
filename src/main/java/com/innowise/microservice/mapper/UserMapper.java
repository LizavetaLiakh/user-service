package com.innowise.microservice.mapper;

import com.innowise.microservice.dto.UserRequestDto;
import com.innowise.microservice.dto.UserResponseDto;
import com.innowise.microservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import java.util.List;

/**
 * Mapper for converting between User entity, UserRequestDto and UserResponseDto.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    /**
     * Maps User entity to UserResponseDto.
     * @param user entity object that needs to be mapped
     * @return UserResponseDto object
     */
    UserResponseDto toUserResponseDto(User user);

    /**
     * Maps list of Users to list of UserResponseDto objects.
     * @param users list of entity objects that need to be mapped
     * @return list of UserResponseDto objects
     */
    List<UserResponseDto> toUserResponseDtoList(List<User> users);

    /**
     * Maps UserRequestDto to User entity.
     * @param userRequestDto DTO object that needs to be mapped
     * @return User entity
     */
    User toUser(UserRequestDto userRequestDto);

    /**
     * Maps list of UserRequestDto to list of User entities.
     * @param userRequestDtos list of DTOs that need to be mapped
     * @return list of User entities
     */
    List<User> toUserList(List<UserRequestDto> userRequestDtos);
}
