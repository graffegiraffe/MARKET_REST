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
            @ApiResponse(responseCode = "201", description = "User successfully created."),
            @ApiResponse(responseCode = "409", description = "Conflict while creating the user.")
    })
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("Received request to create user: {}", user);

        Optional<User> createdUser = userService.createUser(user);
        if (createdUser.isPresent()) {
            logger.info("User created successfully: {}", createdUser.get());
            return new ResponseEntity<>(createdUser.get(), HttpStatus.CREATED);
        } else {
            logger.error("Failed to create user: {}", user);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully retrieved."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable("id") @Parameter(description = "User ID") Long id) {
        logger.info("Received request to fetch user with ID: {}", id);

        return userService.getUserById(id)
                .map(user -> {
                    logger.info("User successfully fetched: {}", user);
                    return new ResponseEntity<>(user, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    logger.warn("User with ID {} not found", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated."),
            @ApiResponse(responseCode = "409", description = "Conflict while updating the user.")
    })
    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        logger.info("Received request to update user: {}", user);

        Optional<User> updatedUser = userService.updateUser(user);
        if (updatedUser.isPresent()) {
            logger.info("User updated successfully: {}", updatedUser.get());
            return new ResponseEntity<>(updatedUser.get(), HttpStatus.OK);
        } else {
            logger.error("Failed to update user: {}", user);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted."),
            @ApiResponse(responseCode = "404", description = "User not found or could not be deleted.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") @Parameter(description = "User ID") Long id) {
        logger.info("Received request to delete user with ID: {}", id);

        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            logger.info("User with ID {} deleted successfully", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.warn("Failed to delete user with ID {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}