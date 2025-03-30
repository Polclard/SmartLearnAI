package org.smartlearnai.config.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtToPrincipalConverter {
    public UserPrincipal convert(DecodedJWT jwt) {
        return UserPrincipal.builder()
                .userId(Long.parseLong(jwt.getSubject()))
                .email(jwt.getClaim("e").asString())
                .authorities(getClaimOrEmptyList(jwt))
                .build();

    }

    private List<SimpleGrantedAuthority> getClaimOrEmptyList(DecodedJWT jwt) {
        var claims = jwt.getClaim("a");
        if (claims.isNull() || claims.isMissing()) return List.of();
        return claims.asList(SimpleGrantedAuthority.class);
    }

}
