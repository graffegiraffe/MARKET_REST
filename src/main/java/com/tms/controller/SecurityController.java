package com.tms.controller;

import com.tms.model.dto.RegistrationRequestDto;
import com.tms.service.RegistrationService;
import com.tms.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security")
public class SecurityController {

    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

    private final SecurityService securityService;
    private final RegistrationService registrationService;

    @Autowired
    public SecurityController(SecurityService securityService, RegistrationService registrationService) {
        this.securityService = securityService;
        this.registrationService = registrationService;
    }

    @Operation(summary = "User registration", description = "Endpoint allows registering a new user. Performs validation and saves User and Security entities.")
    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody @Valid RegistrationRequestDto requestDto,
                                               BindingResult bindingResult) {
        logger.info("Received registration request for user: {}", requestDto.getLogin());

        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors for user: {}", requestDto.getLogin());
            return new ResponseEntity<>("Validation error.", HttpStatus.BAD_REQUEST);
        }

        if (securityService.isLoginUsed(requestDto.getLogin())) {
            logger.warn("Login '{}' is already in use.", requestDto.getLogin());
            return new ResponseEntity<>("Login is already in use.", HttpStatus.CONFLICT);
        }

        try {
            registrationService.register(requestDto);
            logger.info("User '{}' successfully registered.", requestDto.getLogin());
            return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Registration error: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            logger.error("Unhandled error during registration.", e);
            return new ResponseEntity<>("An error occurred during registration.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}