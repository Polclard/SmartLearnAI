package org.smartlearnai.controller.auth;

import lombok.RequiredArgsConstructor;
import org.smartlearnai.model.User;
import org.smartlearnai.model.auth.LoginRequest;
import org.smartlearnai.model.auth.LoginResponse;
import org.smartlearnai.model.auth.RegisterRequest;
import org.smartlearnai.config.security.UserPrincipal;
import org.smartlearnai.model.exeptions.EmailException;
import org.smartlearnai.model.exeptions.InvalidArgumentsException;
import org.smartlearnai.model.exeptions.UserNotFoundException;
import org.smartlearnai.service.AuthService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(authService.attemptLogin(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(LoginResponse.builder().token(exception.getMessage()).build());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Validated RegisterRequest registerRequest) {
        try {
            User user = authService.register(registerRequest);
            return ResponseEntity.ok(user.toString());
        } catch (EmailException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
