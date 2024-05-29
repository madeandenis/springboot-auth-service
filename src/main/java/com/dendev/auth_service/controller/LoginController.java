package com.dendev.auth_service.controller;

import com.dendev.auth_service.model.Role;
import com.dendev.auth_service.payload.request.LoginRequest;
import com.dendev.auth_service.payload.response.MessageResponse;
import com.dendev.auth_service.payload.response.TokenResponse;
import com.dendev.auth_service.services.JwtService;
import com.dendev.auth_service.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class LoginController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @PostMapping({"/signin", "/login"})
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtService.generateToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> userRoles = getUserRolesAsString(userDetails);

            TokenResponse tokenResponse= TokenResponse.builder()
                    .token(jwt)
                    .tokenType("Bearer")
                    .id(userDetails.getId())
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .roles(userRoles)
                    .build();

            return ResponseEntity.ok(
                    new MessageResponse(MessageResponse.MessageType.success.toString(),"Authenticated successfully!")
            );

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse(MessageResponse.MessageType.error.toString(),"Invalid username or password!"));
        }
    }

    private List<String> getUserRolesAsString(UserDetailsImpl userDetails){
        return userDetails.getAuthorities().stream()
                                           .map(GrantedAuthority::getAuthority)
                                           .toList();
    }
}
