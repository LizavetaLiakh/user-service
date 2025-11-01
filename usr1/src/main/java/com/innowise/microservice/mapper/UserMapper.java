package com.innowise.microservice.mapper;

import com.innowise.microservice.dto.UserDto;
import com.innowise.microservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import java.util.List;

/**
 * Mapper for converting UserDto to User and User to UserDto.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    /**
     * Maps User to UserDto.
     * @param user entity object that needs to be mapped
     * @return UserDto object
     */
    UserDto toUserDto(User user);

    /**
     * Maps list of Users to list of UserDto objects.
     * @param users list of entity objects that need to be mapped
     * @return list of UserDto objects
     */
    List<UserDto> toUserDtoList(List<User> users);

    /**
     * Maps UserDto to User.
     * @param userDto DTO object that needs to be mapped
     * @return User object
     */
    User toUser(UserDto userDto);

    /**
     * Maps list of UserDtos to list of User objects.
     * @param userDtos list of DTO objects that need to be mapped
     * @return list of User objects
     */
    List<User> toUserList(List<UserDto> userDtos);
}
