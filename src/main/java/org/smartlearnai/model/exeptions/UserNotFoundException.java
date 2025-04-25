package org.smartlearnai.model.exeptions;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    String email;

    public UserNotFoundException(String email) {
        super("User with email " + email + " not found");
        this.email = email;
    }

}
