package org.smartlearnai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import javax.swing.*;

@Configuration
public class SecurityConfig {

    //region Initial Disabling of Spring Security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Allow all requests
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection if needed
                .formLogin(AbstractHttpConfigurer::disable) // Disable form login
                .httpBasic(AbstractHttpConfigurer::disable); // Disable basic auth
        return http.build();
    }
    //endregion
}
