package com.innowise.microservice.integration;

import com.innowise.microservice.dto.UserRequestDto;
import com.innowise.microservice.dto.UserResponseDto;
import com.innowise.microservice.exception.EmptyUserListException;
import com.innowise.microservice.exception.UserEmailExistsException;
import com.innowise.microservice.exception.UserNotFoundException;
import com.innowise.microservice.exception.UserWithEmailNotFoundException;
import com.innowise.microservice.repository.UserRepository;
import com.innowise.microservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repository;

    @Test
    void testCreateAndGetUser() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("Hanna");
        userRequestDto.setSurname("Montana");
        userRequestDto.setBirthDate(LocalDate.of(2000, 3, 20));
        userRequestDto.setEmail("hanna00@gmail.com");

        UserResponseDto userResponseDto = service.createUser(userRequestDto);

        assertNotNull(userResponseDto);
        assertEquals("Hanna", userResponseDto.getName());
        assertEquals("Montana", userResponseDto.getSurname());
        assertEquals(LocalDate.of(2000, 3, 20), userResponseDto.getBirthDate());
        assertEquals("hanna00@gmail.com", userResponseDto.getEmail());

        UserResponseDto foundUserResponseDto = service.getUserById(userResponseDto.getId());

        assertEquals(userResponseDto.getId(), foundUserResponseDto.getId());
    }

    @Test
    void testGetUserByNonExistingId() {
        Long nonExistingId = 105L;
        assertThrows(UserNotFoundException.class, () -> service.getUserById(nonExistingId));
    }

    @Test
    void testGetUserByNonExistingEmail() {
        String nonExistingEmail = "abc@gmail.com";
        assertThrows(UserWithEmailNotFoundException.class, () -> service.getUserByEmail(nonExistingEmail));
    }

    @Test
    void testGetUsersByIdsEmpty() {
        List<Long> ids = List.of(100L, 101L);
        assertThrows(EmptyUserListException.class, () -> service.getUsersByIds(ids));
    }

    @Test
    void testUpdateUserById() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("Patrick");
        userRequestDto.setSurname("Wong");
        userRequestDto.setBirthDate(LocalDate.of(1995, 5, 15));
        userRequestDto.setEmail("pat@gmail.com");

        UserResponseDto userResponseDto = service.createUser(userRequestDto);

        UserRequestDto updateUserRequestDto = new UserRequestDto();
        updateUserRequestDto.setName("Pat");
        updateUserRequestDto.setSurname("Wang");
        updateUserRequestDto.setBirthDate(LocalDate.of(1994, 5, 15));
        updateUserRequestDto.setEmail("pattt@gmail.com");

        UserResponseDto updatedUserResponseDto = service.updateUserById(userResponseDto.getId(), updateUserRequestDto);

        assertEquals("Pat", updatedUserResponseDto.getName());
        assertEquals("Wang", updatedUserResponseDto.getSurname());
        assertEquals(LocalDate.of(1994, 5, 15), updatedUserResponseDto.getBirthDate());
        assertEquals("pattt@gmail.com", updatedUserResponseDto.getEmail());
    }

    @Test
    void testCreateUserByIdEmailExists() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("Tom");
        userRequestDto.setSurname("Young");
        userRequestDto.setBirthDate(LocalDate.of(1995, 2, 2));
        userRequestDto.setEmail("tom@gmail.com");

        UserResponseDto userResponseDto = service.createUser(userRequestDto);

        UserRequestDto userRequestDtoSameEmail = new UserRequestDto();
        userRequestDtoSameEmail.setName("Tommy");
        userRequestDtoSameEmail.setSurname("Cash");
        userRequestDtoSameEmail.setBirthDate(LocalDate.of(1995, 2, 20));
        userRequestDtoSameEmail.setEmail("tom@gmail.com");

        assertThrows(UserEmailExistsException.class, () -> service.createUser(userRequestDtoSameEmail));
    }

    @Test
    void testDeleteUserById() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("Mary");
        userRequestDto.setSurname("Alice");
        userRequestDto.setBirthDate(LocalDate.of(2003, 1, 22));
        userRequestDto.setEmail("alicemary@gmail.com");

        UserResponseDto userResponseDto = service.createUser(userRequestDto);

        service.deleteUserById(userResponseDto.getId());

        assertThrows(UserNotFoundException.class, () -> service.getUserById(userResponseDto.getId()));
    }
}
