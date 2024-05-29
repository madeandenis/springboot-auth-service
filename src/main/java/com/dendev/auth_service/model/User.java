package com.dendev.auth_service.model;

import com.dendev.auth_service.validation.ValidationConstraints;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @NotBlank
    @Size(min = ValidationConstraints.USERNAME_MIN_LENGTH, max = ValidationConstraints.USERNAME_MAX_LENGTH)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = ValidationConstraints.PASSWORD_MIN_LENGTH, max = ValidationConstraints.PASSWORD_MAX_LENGTH)
    private String password;

    private Set<Role> roles = new HashSet<>();
}
