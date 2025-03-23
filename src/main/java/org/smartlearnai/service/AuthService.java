package org.smartlearnai.service;

import org.smartlearnai.model.User;
import org.smartlearnai.model.auth.LoginResponse;
import org.smartlearnai.model.auth.RegisterRequest;

import java.util.Optional;

public interface AuthService {
    LoginResponse attemptLogin(String email, String password);

    User register(RegisterRequest registerRequest);

    public Optional<User> findByEmail(String email);
}
