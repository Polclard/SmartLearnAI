package org.smartlearnai.config;

import lombok.RequiredArgsConstructor;
import org.smartlearnai.config.security.CustomUserDetailService;
import org.smartlearnai.config.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //region Initial Disabling of Spring Security
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Allow all requests
//                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection if needed
//                .formLogin(AbstractHttpConfigurer::disable) // Disable form login
//                .httpBasic(AbstractHttpConfigurer::disable); // Disable basic auth
//        return http.build();
//    }

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailService customUserDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http
//                .cors().disable()
//                .csrf().disable()
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/h2-console").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/auth/login").permitAll()
                                .requestMatchers("/auth/register").permitAll()
                                .requestMatchers(("/auth/auth")).authenticated()
                                //.requestMatchers(("/api/**")).permitAll()
                                .anyRequest().authenticated()) // Allow all requests
//                                .anyRequest().permitAll()) // Allow all requests
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection if needed
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin // for H2 error = X-Frame-Options' to 'deny'
                        )
                )
                .formLogin(AbstractHttpConfigurer::disable) // Disable form login
                .httpBasic(AbstractHttpConfigurer::disable); // Disable basic auth
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder());
        return builder.build();

    }
}
