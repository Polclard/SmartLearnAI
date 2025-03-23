package org.smartlearnai.controller.auth;

import lombok.RequiredArgsConstructor;
import org.smartlearnai.model.User;
import org.smartlearnai.model.auth.LoginRequest;
import org.smartlearnai.model.auth.LoginResponse;
import org.smartlearnai.model.auth.RegisterRequest;
import org.smartlearnai.config.security.UserPrincipal;
import org.smartlearnai.service.AuthService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @GetMapping("/auth")
    public String auth(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return "auth " + userPrincipal.getEmail() + " with id =  " + userPrincipal.getUserId();
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest loginRequest) {
        return authService.attemptLogin(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/register")
    public String register(@RequestBody @Validated RegisterRequest registerRequest) {
        User user = authService.register(registerRequest);
        return user.toString();
    }
}
