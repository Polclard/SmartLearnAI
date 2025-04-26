package org.smartlearnai.model.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterRequest {

    private final String email;
    private final String password;
//    private final String role;
    private final int age;

}
