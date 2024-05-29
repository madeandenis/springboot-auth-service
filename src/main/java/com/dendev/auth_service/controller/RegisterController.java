package com.dendev.auth_service.controller;

import com.dendev.auth_service.model.ERole;
import com.dendev.auth_service.model.Role;
import com.dendev.auth_service.model.User;
import com.dendev.auth_service.payload.request.RegisterRequest;
import com.dendev.auth_service.payload.response.MessageResponse;
import com.dendev.auth_service.repository.RoleRepository;
import com.dendev.auth_service.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class RegisterController {
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @PostMapping({"/register","/signup"})
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder("Validation failed: ");
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage()).append(". ");
            }
            return ResponseEntity.badRequest().body(new MessageResponse(MessageResponse.MessageType.error.toString(),errorMessage.toString()));
        }

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageResponse.MessageType.error.toString(),"Username is already taken!"));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageResponse.MessageType.error.toString(),"Email is already in use!"));
        }

        User user = User.builder()
                         .username(registerRequest.getUsername())
                         .email(registerRequest.getEmail())
                         .password(encoder.encode(registerRequest.getPassword()))
                         .build();


        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                      .orElseThrow(() -> new RuntimeException("Role not found."));
        roles.add(userRole);

        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse(MessageResponse.MessageType.success.toString(),"User registered successfully!"));
    }
}
