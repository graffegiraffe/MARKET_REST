package com.tms.controller;

import com.tms.model.dto.RegistrationRequestDto;
import com.tms.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecurityController {

    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

    private final SecurityService securityService;

    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Operation(summary = "User registration", description = "Endpoint allows to register a new user. Checks validation. In the database creates 2 new models related to each other (User, Security)")
    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid RegistrationRequestDto requestDto,
                                                   BindingResult bindingResult) {
        logger.info("Received registration request for user: {}", requestDto.getLogin());

        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors occurred for user: {}", requestDto.getLogin());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Boolean result = securityService.registration(requestDto);
        if (result) {
            logger.info("User {} successfully registered", requestDto.getLogin());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            logger.error("Failed to register user: {}", requestDto.getLogin());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}