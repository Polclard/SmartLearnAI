package org.smartlearnai.service.impl;

import org.smartlearnai.model.User;
import org.smartlearnai.repository.UserRepository;
import org.smartlearnai.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserServiceImpl implements UserService {
    private static final String EXISTING_EMAIL = "test@test.com";

    private static final String OTHER_EMAIL = "other@test.com";

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user;
    }

}
