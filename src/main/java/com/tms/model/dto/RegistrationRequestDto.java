package com.tms.model.dto;

import com.tms.annotation.CustomAge;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "RegistrationDTO")
public class RegistrationRequestDto {
    @NotNull(message = "Firstname cannot be null")
    @Size(min = 2, max = 20, message = "Firstname must be between 2 and 20 characters")
    private String firstname;

    @NotNull(message = "Second name cannot be null")
    @Size(min = 2, max = 20, message = "Second name must be between 2 and 20 characters")
    private String secondName;

    @CustomAge
    private Integer age;

    @Email(message = "Invalid email format")
    private String email;

    private String sex;

    @Pattern(regexp = "[0-9]{12}", message = "Telephone number must consist of 12 digits")
    private String telephoneNumber;

    @NotNull(message = "Login cannot be null")
    @NotBlank(message = "Login cannot be blank")
    @Size(max = 20, message = "Login cannot exceed 20 characters")
    private String login;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    private String password;

}