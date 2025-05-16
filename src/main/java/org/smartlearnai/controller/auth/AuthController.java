package org.smartlearnai.controller.auth;

import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.smartlearnai.model.User;
import org.smartlearnai.model.auth.AuthResponse;
import org.smartlearnai.model.auth.LoginRequest;
import org.smartlearnai.model.auth.LoginResponse;
import org.smartlearnai.model.auth.RegisterRequest;
import org.smartlearnai.config.security.UserPrincipal;
import org.smartlearnai.model.dto.UserResponseDto;
import org.smartlearnai.model.exeptions.EmailException;
import org.smartlearnai.model.exeptions.InvalidArgumentsException;
import org.smartlearnai.model.exeptions.UserNotFoundException;
import org.smartlearnai.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    @GetMapping("/auth")
    public String auth(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return "auth " + userPrincipal.getEmail() + " with id =  " + userPrincipal.getUserId();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(authService.attemptLogin(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(LoginResponse.builder().token(exception.getMessage()).build());
        }
    }

    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Validated RegisterRequest registerRequest) {
        try {
            // Call service
            User user = authService.register(registerRequest);

            // Convert to DTO
            UserResponseDto userDto = new UserResponseDto(user);

            // Create response
            AuthResponse response = new AuthResponse(
                    true,
                    "User registered successfully",
                    userDto
            );

            return ResponseEntity.ok(response);

        } catch (EmailException e) {
            logger.warn("Email exception: {}", e.getMessage());

            // Create error response
            AuthResponse errorResponse = new AuthResponse(
                    false,
                    e.getMessage() != null ? e.getMessage() : "Email validation failed"
            );

            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            logger.error("Unexpected error during registration", e);

            // Create error response
            AuthResponse errorResponse = new AuthResponse(
                    false,
                    "Registration failed: " + e.getMessage()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
