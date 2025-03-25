package com.tms.controller;

import com.tms.model.User;
import com.tms.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiResponses(value = {
            @ApiResponse(description = "When user created.", responseCode = "201"),
            @ApiResponse(description = "When something wrong.", responseCode = "409")
    })
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("Received request to create user: {}", user);

        Optional<User> createdUser = userService.createUser(user);
        if (createdUser.isEmpty()) {
            logger.error("Failed to create user: {}", user);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        logger.info("User created successfully: {}", createdUser.get());
        return new ResponseEntity<>(createdUser.get(), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") @Parameter(description = "User id") Long id) {
        logger.info("Received request to fetch user by ID: {}", id);

        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            logger.warn("User with ID {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("User with ID {} fetched successfully: {}", id);
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        logger.info("Received request to update user: {}", user);

        Optional<User> userUpdated = userService.updateUser(user);
        if (userUpdated.isEmpty()) {
            logger.error("Failed to update user: {}", user);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        logger.info("User updated successfully: {}", userUpdated.get());
        return new ResponseEntity<>(userUpdated.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") @Parameter(description = "User id") Long userId) {
        logger.info("Received request to delete user with ID: {}", userId);

        Optional<User> userDeleted = userService.deleteUser(userId);
        if (userDeleted.isEmpty()) {
            logger.warn("Failed to delete user with ID: {}", userId);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        logger.info("User with ID {} deleted successfully", userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}