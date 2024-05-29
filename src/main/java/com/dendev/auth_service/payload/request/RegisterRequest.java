package com.dendev.auth_service.payload.request;

import com.dendev.auth_service.validation.ValidationConstraints;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest{
    @NotBlank(message = "Username is mandatory")
    @Size(min = ValidationConstraints.USERNAME_MIN_LENGTH,
            max = ValidationConstraints.USERNAME_MAX_LENGTH,
            message = "Username must have at least {min} characters")
    private String username;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = ValidationConstraints.PASSWORD_MIN_LENGTH,
            max = ValidationConstraints.PASSWORD_MAX_LENGTH,
            message = "Password must have at least {min} characters")
    private String password;
}
