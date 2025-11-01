package com.innowise.microservice.service;

import com.innowise.microservice.dto.UserDto;
import com.innowise.microservice.entity.User;
import com.innowise.microservice.exception.EmptyUserListException;
import com.innowise.microservice.exception.UserEmailExistsException;
import com.innowise.microservice.exception.UserNotFoundException;
import com.innowise.microservice.exception.UserWithEmailNotFoundException;
import com.innowise.microservice.mapper.UserMapper;
import com.innowise.microservice.repository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
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
        repository.findByEmail(userDto.getEmail())
                .ifPresent(sameEmailUser -> {
                    throw new UserEmailExistsException(userDto.getEmail());
                });
        User user = mapper.toUser(userDto);
        User savedUser = repository.save(user);
        return mapper.toUserDto(savedUser);
    }

    /**
     * Finds a user by id.
     * @param id user's unique identifier
     * @return user as DTO if found, empty if not found
     */
    public UserDto getUserById(Long id) {
        return repository.findById(id)
                .map(mapper::toUserDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Finds users by ids.
     * @param ids list of users' unique identifiers
     * @return list of users as DTOs
     */
    public List<UserDto> getUsersByIds(Iterable<Long> ids) {
        List<UserDto> users = repository.findAllById(ids)
                .stream()
                .map(mapper::toUserDto)
                .toList();
        if (users.isEmpty()) {
            throw new EmptyUserListException(ids);
        }
        return users;
    }

    /**
     * Finds a user by email.
     * @param email user's email
     * @return user as DTO if found, empty if not found
     */
    public UserDto getUserByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toUserDto)
                .orElseThrow(() -> new UserWithEmailNotFoundException(email));
    }

    /**
     * Updates a user by id.
     * @param id user's unique identifier
     * @param newUserDto UserDto that contains current data
     */
    @Transactional
    public void updateUserById(Long id, UserDto newUserDto) {
        repository.findByEmail(newUserDto.getEmail())
                .ifPresent(sameEmailUser -> {
                    throw new UserEmailExistsException(newUserDto.getEmail());
                });
        int updated = repository.updateUser(id, newUserDto.getName(), newUserDto.getSurname(),
                newUserDto.getBirthDate(), newUserDto.getEmail());
        if (updated == 0) {
            throw new UserNotFoundException(id);
        }
    }

    /**
     * Deletes a user by id.
     * @param id user's id
     */
    @Transactional
    public void deleteUserById(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id);
        }
    }
}