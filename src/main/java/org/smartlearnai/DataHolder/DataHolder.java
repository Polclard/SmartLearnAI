package org.smartlearnai.DataHolder;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.smartlearnai.model.User;
import org.smartlearnai.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataHolder {
    public static List<User> users = null;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataHolder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    public void init() {

        users = new ArrayList<>();

        if (userRepository.count() == 0) {
            String password = passwordEncoder.encode("test");
            users.add(new User(null, "test@test.com", password, "USER", 12));
            users.add(new User(null, "admin@admin.com", password, "ADMIN", 12));// password = test

            userRepository.saveAll(users);
        }
    }
}
