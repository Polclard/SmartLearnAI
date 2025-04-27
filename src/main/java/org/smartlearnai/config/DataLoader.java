package org.smartlearnai.config;

import lombok.RequiredArgsConstructor;
import org.smartlearnai.model.auth.RegisterRequest;
import org.smartlearnai.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final AuthService authService;

    @Bean
    CommandLineRunner loadDefaultUser() {
        return args -> {
            try {
                RegisterRequest registerRequest = RegisterRequest.builder()
                        .email("testuser@example.com")
                        .password("securePassword123")
                        .age(25)
                        .build();

                authService.register(registerRequest);
                System.out.println("User registered successfully.");
            } catch (Exception e) {
                System.out.println("User registration failed: " + e.getMessage());
            }
        };
    }
}
