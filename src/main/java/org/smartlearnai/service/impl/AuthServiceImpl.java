package org.smartlearnai.service.impl;

import lombok.RequiredArgsConstructor;
import org.smartlearnai.model.User;
import org.smartlearnai.model.auth.LoginResponse;
import org.smartlearnai.model.auth.RegisterRequest;
import org.smartlearnai.repository.UserRepository;
import org.smartlearnai.config.security.JwtIssuer;
import org.smartlearnai.config.security.UserPrincipal;
import org.smartlearnai.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse attemptLogin(String email, String password) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var principal = (UserPrincipal) authentication.getPrincipal();
        var roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);
        return LoginResponse.builder()
                .token(token)
                .build();
    }

    public User register(RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(registerRequest.getRole());
        user.setAge(registerRequest.getAge());
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user;
    }
}
