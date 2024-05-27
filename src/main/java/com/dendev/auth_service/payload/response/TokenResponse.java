package com.dendev.auth_service.payload.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    @NotBlank
    private String token;
    @NotBlank
    private String tokenType = "Bearer";
    @NotBlank
    private String id;
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private List<String> roles;
}
