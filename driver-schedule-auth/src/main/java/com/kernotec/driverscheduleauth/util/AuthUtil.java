package com.kernotec.driverscheduleauth.util;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthUtil {

    public UUID getUserIdFromAuthentication(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken authenticationJwt) {
            return UUID.fromString(authenticationJwt.getToken()
                .getSubject());
        }

        return null;
    }
}
