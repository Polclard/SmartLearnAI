package org.smartlearnai.service.impl;

import lombok.RequiredArgsConstructor;
import org.smartlearnai.model.User;
import org.smartlearnai.model.auth.LoginResponse;
import org.smartlearnai.model.auth.RegisterRequest;
import org.smartlearnai.model.exeptions.EmailException;
import org.smartlearnai.model.exeptions.InvalidArgumentsException;
import org.smartlearnai.model.exeptions.UserNotFoundException;
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
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse attemptLogin(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        Optional<User> user = findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException(email);
        }

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
        if (!isValid(registerRequest.getEmail())) {
            throw new EmailException();
        }
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole("user");
        user.setAge(registerRequest.getAge());
        return userRepository.save(user);
    }

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public boolean isValid(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }

        // Additional length check (RFC 3696 says max 254 chars for email)
        if (email.length() > 254) {
            return false;
        }

        return EMAIL_PATTERN.matcher(email).matches();
    }

    public Optional<User> findByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user;
    }
}
