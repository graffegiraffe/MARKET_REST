package com.tms.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Scope("prototype")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class Security {

    private Long id;

    @NotBlank(message = "Login cannot be blank")
    @Size(min = 4, max = 50, message = "Login must be between 4 and 50 characters")
    private String login;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    @NotNull(message = "Role cannot be null")
    private Role role;

    private Timestamp created;
    private Timestamp updated;

    @NotNull(message = "User ID cannot be null")
    private Long userId;
}
