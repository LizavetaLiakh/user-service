package com.innowise.microservice.service;

import com.innowise.microservice.dto.UserDto;
import com.innowise.microservice.entity.User;
import com.innowise.microservice.mapper.UserMapper;
import com.innowise.microservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

/**
 * Service class for managing User entity.
 * <p>
 *     Provides CRUD operations: create, get user by id, get users by ids, get user by email, update user by id,
 *     delete user by id.
 * </p>
 */
@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Creates a new user in the database.
     * @param userDto DTO with new user's data
     * @return created user as DTO
     */
    public UserDto createUser(UserDto userDto) {
        User user = mapper.toUser(userDto);
        User savedUser = repository.save(user);
        return mapper.toUserDto(savedUser);
    }

    /**
     * Finds a user by id.
     * @param id user's unique identifier
     * @return user as DTO if found, empty if not found
     */
    public Optional<UserDto> getUserById(Long id) {
        return repository.findById(id)
                .map(mapper::toUserDto);
    }

    /**
     * Finds users by ids.
     * @param ids list of users' unique identifiers
     * @return list of users as DTOs
     */
    public List<UserDto> getUsersByIds(Iterable<Long> ids) {
        return repository.findAllById(ids)
                .stream()
                .map(mapper::toUserDto)
                .toList();
    }

    /**
     * Finds a user by email.
     * @param email user's email
     * @return user as DTO if found, empty if not found
     */
    public Optional<UserDto> getUserByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toUserDto);
    }

    /**
     * Updates a user by id.
     * @param id user's unique identifier
     * @param newUserDto UserDto that contains current data
     * @return number of updated rows, 0 if user was not found
     */
    @Transactional
    public int updateUserById(Long id, UserDto newUserDto) {
        return repository.updateUser(id, newUserDto.getName(), newUserDto.getSurname(), newUserDto.getBirthDate(),
                newUserDto.getEmail());
    }

    /**
     * Deletes a user by id.
     * @param id user's id
     */
    @Transactional
    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }
}