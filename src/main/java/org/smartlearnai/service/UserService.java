package org.smartlearnai.service;

import org.smartlearnai.model.Course;
import org.smartlearnai.model.User;

import java.util.Optional;
import java.util.Set;

public interface UserService {
    Optional<User> findByEmail(String email);
}
