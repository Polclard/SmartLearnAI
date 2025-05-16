package org.smartlearnai.model.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class LoginRequest {
    @NotBlank
    private final String email;
    @NotBlank
    private final String password;
}
