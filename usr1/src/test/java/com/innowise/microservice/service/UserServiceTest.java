package com.innowise.microservice.service;

import com.innowise.microservice.dto.UserRequestDto;
import com.innowise.microservice.dto.UserResponseDto;
import com.innowise.microservice.entity.User;
import com.innowise.microservice.exception.EmptyUserListException;
import com.innowise.microservice.exception.UserEmailExistsException;
import com.innowise.microservice.exception.UserNotFoundException;
import com.innowise.microservice.exception.UserWithEmailNotFoundException;
import com.innowise.microservice.mapper.UserMapper;
import com.innowise.microservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserMapper mapper;

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private User user;
    private User updatedUser;
    private UserRequestDto requestUserDto;
    private UserResponseDto responseUserDto;
    private UserResponseDto updatedResponseUserDto;

    private final static int USER_LIST_SIZE = 2;
    private final static String EXISTING_EMAIL = "hanna00@gmail.com";
    private final static String NOT_EXISTING_EMAIL = "abc@gmail.com";

    @BeforeEach
    void setUpUsers() {
        MockitoAnnotations.openMocks(this);

        user = new User(1L, "Hanna", "Montana", LocalDate.of(2000, 3, 20)
                , "hanna00@gmail.com");

        requestUserDto = new UserRequestDto();
        requestUserDto.setName("Hanna");
        requestUserDto.setSurname("Montana");
        requestUserDto.setBirthDate(LocalDate.of(2000, 3, 20));
        requestUserDto.setEmail("hanna00@gmail.com");

        responseUserDto = new UserResponseDto();
        responseUserDto.setId(1L);
        responseUserDto.setName("Hanna");
        responseUserDto.setSurname("Montana");
        responseUserDto.setBirthDate(LocalDate.of(2000, 3, 20));
        responseUserDto.setEmail("hanna00@gmail.com");

        updatedUser = new User(1L, "Hanna", "Low", LocalDate.of(2001, 9, 29)
                , "hanna11@gmail.com");

        updatedResponseUserDto = new UserResponseDto();
        updatedResponseUserDto.setId(1L);
        updatedResponseUserDto.setName("Hanna");
        updatedResponseUserDto.setSurname("Low");
        updatedResponseUserDto.setBirthDate(LocalDate.of(2001, 9, 29));
        updatedResponseUserDto.setEmail("hanna11@gmail.com");
    }

    @Test
    void testCreateUserUniqueEmail() {
        when(repository.findByEmail(requestUserDto.getEmail())).thenReturn(Optional.empty());
        when(mapper.toUser(requestUserDto)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toUserResponseDto(user)).thenReturn(responseUserDto);

        UserResponseDto resUserResponseDto = service.createUser(requestUserDto);

        assertNotNull(resUserResponseDto);
        assertEquals(responseUserDto.getEmail(), resUserResponseDto.getEmail());
        verify(repository).findByEmail("hanna00@gmail.com");
        verify(repository).save(user);
        verify(mapper).toUser(requestUserDto);
        verify(mapper).toUserResponseDto(user);
    }

    @Test
    void testCreateUserEmailExists() {
        when(repository.findByEmail(requestUserDto.getEmail())).thenReturn(Optional.of(user));

        UserEmailExistsException ex = assertThrows(UserEmailExistsException.class,
                () -> service.createUser(requestUserDto));
        assertEquals("User with email hanna00@gmail.com already exists", ex.getMessage());
        verify(repository).findByEmail("hanna00@gmail.com");
        verify(repository, never()).save(any());
    }

    @Test
    void testGetUserByIdExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.toUserResponseDto(user)).thenReturn(responseUserDto);

        UserResponseDto resultUserResponseDto = service.getUserById(1L);

        assertNotNull(resultUserResponseDto);
        assertEquals(responseUserDto.getId(), resultUserResponseDto.getId());
        assertEquals("Hanna", resultUserResponseDto.getName());
        assertEquals("Montana", resultUserResponseDto.getSurname());
        assertEquals("hanna00@gmail.com", resultUserResponseDto.getEmail());
        verify(repository).findById(1L);
        verify(mapper).toUserResponseDto(user);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(repository.findById(100L)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> service.getUserById(100L));
        assertEquals("User with id 100 not found", ex.getMessage());
        verify(repository).findById(100L);
        verify(mapper, never()).toUserResponseDto(any());
    }

    @Test
    void testGetUsersByIds() {
        User user2 = new User(2L, "Patrick", "Wong", LocalDate.of(1995, 5, 15)
                , "pat@gmail.com");
        UserResponseDto userResponseDto2 = new UserResponseDto();
        userResponseDto2.setId(user2.getId());
        userResponseDto2.setName(user2.getName());
        userResponseDto2.setSurname(user2.getSurname());
        userResponseDto2.setBirthDate(user2.getBirthDate());
        userResponseDto2.setEmail(user2.getEmail());

        List<Long> ids = List.of(1L, 2L);
        List<User> users = List.of(user, user2);

        when(repository.findAllById(ids)).thenReturn(users);
        when(mapper.toUserResponseDto(user)).thenReturn(responseUserDto);
        when(mapper.toUserResponseDto(user2)).thenReturn(userResponseDto2);

        List<UserResponseDto> resultUsers = service.getUsersByIds(ids);

        assertNotNull(resultUsers);
        assertEquals(USER_LIST_SIZE, resultUsers.size());
        assertEquals("Hanna", resultUsers.get(0).getName());
        assertEquals("Patrick", resultUsers.get(1).getName());
        assertEquals("Montana", resultUsers.get(0).getSurname());
        assertEquals("Wong", resultUsers.get(1).getSurname());
        assertEquals(LocalDate.of(2000, 3, 20), resultUsers.get(0).getBirthDate());
        assertEquals(LocalDate.of(1995, 5, 15), resultUsers.get(1).getBirthDate());
        assertEquals("hanna00@gmail.com", resultUsers.get(0).getEmail());
        assertEquals("pat@gmail.com", resultUsers.get(1).getEmail());

        verify(repository).findAllById(ids);
        verify(mapper).toUserResponseDto(user);
        verify(mapper).toUserResponseDto(user2);
    }

    @Test
    void testGetUsersByIdsEmpty() {
        List<Long> ids = List.of(100L, 101L);

        when(repository.findAllById(ids)).thenReturn(new ArrayList<>());

        EmptyUserListException ex = assertThrows(EmptyUserListException.class, () -> service.getUsersByIds(ids));
        assertEquals("No users found with ids: [100, 101]", ex.getMessage());
        verify(repository).findAllById(ids);
        verify(mapper, never()).toUserResponseDto(any());
    }

    @Test
    void testGetUserByEmail() {
        when(repository.findByEmail(EXISTING_EMAIL)).thenReturn(Optional.of(user));
        when(mapper.toUserResponseDto(user)).thenReturn(responseUserDto);

        UserResponseDto resultUserResponseDto = service.getUserByEmail(EXISTING_EMAIL);

        assertNotNull(resultUserResponseDto);
        assertEquals(EXISTING_EMAIL, resultUserResponseDto.getEmail());
        assertEquals("Hanna", resultUserResponseDto.getName());
        assertEquals("Montana", resultUserResponseDto.getSurname());
        assertEquals(LocalDate.of(2000, 3, 20), resultUserResponseDto.getBirthDate());
        verify(repository).findByEmail(EXISTING_EMAIL);
        verify(mapper).toUserResponseDto(user);
    }

    @Test
    void testGetUserByEmailNotFound() {
        when(repository.findByEmail(NOT_EXISTING_EMAIL)).thenReturn(Optional.empty());

        UserWithEmailNotFoundException ex = assertThrows(UserWithEmailNotFoundException.class,
                () -> service.getUserByEmail(NOT_EXISTING_EMAIL));
        assertEquals("User with email abc@gmail.com not found", ex.getMessage());
        verify(repository).findByEmail(NOT_EXISTING_EMAIL);
        verify(mapper, never()).toUserResponseDto(any());
    }

    @Test
    void testUpdateUser() {
        requestUserDto.setSurname("Low");
        requestUserDto.setBirthDate(LocalDate.of(2001, 9, 29));
        requestUserDto.setEmail("hanna11@gmail.com");

        when(repository.findByEmail(requestUserDto.getEmail())).thenReturn(Optional.empty());
        when(repository.updateUser(eq(1L), anyString(), anyString(), any(), anyString())).thenReturn(1);
        when(repository.findById(1L)).thenReturn(Optional.of(updatedUser));
        when(mapper.toUserResponseDto(updatedUser)).thenReturn(updatedResponseUserDto);

        UserResponseDto resultUserResponseDto = service.updateUserById(1L, requestUserDto);

        assertNotNull(resultUserResponseDto);
        assertEquals("Hanna", resultUserResponseDto.getName());
        assertEquals("Low", resultUserResponseDto.getSurname());
        assertEquals(LocalDate.of(2001, 9, 29), resultUserResponseDto.getBirthDate());
        assertEquals("hanna11@gmail.com", resultUserResponseDto.getEmail());

        verify(repository).findByEmail("hanna11@gmail.com");
        verify(repository).updateUser(1L, "Hanna", "Low"
                , LocalDate.of(2001, 9, 29), "hanna11@gmail.com");
        verify(repository).findById(1L);
        verify(mapper).toUserResponseDto(updatedUser);
    }

    @Test
    void testUpdateUserEmailExists() {
        requestUserDto.setEmail("hanna_new_email@gmail.com");
        requestUserDto.setName("Hanna");
        requestUserDto.setSurname("Lays");
        requestUserDto.setBirthDate(LocalDate.of(2000, 3, 20));

        User sameEmailUser = new User(2L, "Patrick", "Wong"
                , LocalDate.of(1995, 5, 15), "hanna_new_email@gmail.com");

        when(repository.findByEmail(requestUserDto.getEmail())).thenReturn(Optional.of(sameEmailUser));

        UserEmailExistsException ex = assertThrows(UserEmailExistsException.class,
                () -> service.updateUserById(1L, requestUserDto));
        assertEquals("User with email hanna_new_email@gmail.com already exists", ex.getMessage());
        verify(repository).findByEmail("hanna_new_email@gmail.com");
        verify(repository, never()).updateUser(anyLong(), anyString(), anyString(), any(), anyString());
    }

    @Test
    void testUpdateUserNotFound() {
        requestUserDto.setEmail("hanna_new_email@gmail.com");
        requestUserDto.setName("Hanna");
        requestUserDto.setSurname("Lays");
        requestUserDto.setBirthDate(LocalDate.of(2000, 3, 20));

        when(repository.findByEmail(requestUserDto.getEmail())).thenReturn(Optional.empty());
        when(repository.updateUser(eq(1L), eq(requestUserDto.getName()), eq(requestUserDto.getSurname()),
                eq(requestUserDto.getBirthDate()), eq(requestUserDto.getEmail()))).thenReturn(1);
        when(repository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> service.updateUserById(1L, requestUserDto));
        assertEquals("User with id 1 not found", ex.getMessage());
        verify(repository).findByEmail("hanna_new_email@gmail.com");
        verify(repository).updateUser(1L, "Hanna", "Lays",
                LocalDate.of(2000, 3, 20), "hanna_new_email@gmail.com");
        verify(repository).findById(1L);
        verify(mapper, never()).toUserResponseDto(any());
    }

    @Test
    void testDeleteUserById() {
        assertDoesNotThrow(() -> service.deleteUserById(1L));
        verify(repository).deleteById(1L);
    }

    @Test
    void testDeleteUserByIdNotFound() {
        doThrow(new org.springframework.dao.EmptyResultDataAccessException(1)).when(repository).deleteById(100L);
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> service.deleteUserById(100L));
        assertEquals("User with id 100 not found", ex.getMessage());
        verify(repository).deleteById(100L);
    }
}
