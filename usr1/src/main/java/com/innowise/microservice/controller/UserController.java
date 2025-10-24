package com.innowise.microservice.controller;

import com.innowise.microservice.dto.UserDto;
import com.innowise.microservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

/**
 * REST-controller for user management.
 * <p>
 * Provides CRUD-operations:
 * <ul>
 *     <li>Creating a new user</li>
 *     <li>Getting a user by id</li>
 *     <li>Getting a user by e-mail</li>
 *     <li>Getting a list of users by their ids</li>
 *     <li>Updating a user by id</li>
 *     <li>Deleting a user by id</li>
 * </ul>
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * Creates a new user.
     *
     * @param userDto New user's data.
     * @return Created user.
     * @throws com.innowise.microservice.exception.UserEmailExistsException If given email is already registered for
     * another user.
     * @response 201 Created - New user successfully created.
     * @response 409 Conflict - A user with the given email already exists.
     * @response 500 Internal Server Error - Unexpected server error occurred.
     */
    @PostMapping("/add")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        UserDto newUser = service.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    /**
     * Finds a user by id.
     *
     * @param id User's id.
     * @return Found user.
     * @throws com.innowise.microservice.exception.UserNotFoundException If there's no user with given id.
     * @response 200 OK - User found.
     * @response 404 Not Found - User not found.
     * @response 500 Internal Server Error - Unexpected server error occurred.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = service.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    /**
     * Finds users by their ids.
     *
     * @param ids A list of users' ids.
     * @return A list of found users.
     * @throws com.innowise.microservice.exception.EmptyUserListException If there's no users with given ids.
     * @response 200 OK - Users found.
     * @response 404 Not Found - Users not found.
     * @response 500 Internal Server Error - Unexpected server error occurred.
     */
    @GetMapping("/get")
    public ResponseEntity<List<UserDto>> getUsersByIds(@RequestParam List<Long> ids) {
        List<UserDto> users = service.getUsersByIds(ids);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    /**
     * Finds a user by e-mail.
     *
     * @param email User's e-mail.
     * @return Found user.
     * @throws com.innowise.microservice.exception.UserEmailExistsException If there's no user with given e-mail.
     * @response 200 OK - User found.
     * @response 404 Not Found - User not found.
     * @response 500 Internal Server Error - Unexpected server error occurred.
     */
    @GetMapping("/get/email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        UserDto user = service.getUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    /**
     * Updates a user with given id.
     *
     * @param id Identifier of the user that should be updated.
     * @param userDto New data of the user.
     * @throws com.innowise.microservice.exception.UserNotFoundException If there's no user with given id.
     * @throws com.innowise.microservice.exception.UserEmailExistsException If a user with the given email already
     * exists.
     * @response 200 OK - User successfully updated.
     * @response 404 Not Found - User not found.
     * @response 409 Conflict - A user with the given email already exists.
     * @response 500 Internal Server Error - Unexpected server error occurred.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updatedUser = service.updateUserById(id, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    /**
     * Deletes a user with given id.
     *
     * @param id Identifier of the user that should be deleted.
     * @throws com.innowise.microservice.exception.UserNotFoundException If there's no user with given id.
     * @response 204 No Content - User successfully deleted.
     * @response 404 Not Found - User not found.
     * @response 500 Internal Server Error - Unexpected server error occurred.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
