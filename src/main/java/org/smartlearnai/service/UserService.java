package org.smartlearnai.service;

import org.smartlearnai.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
}
