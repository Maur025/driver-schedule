package com.kernotec.driverscheduleservice.util;

import com.kernotec.driverscheduleservice.exception.PersonException;
import com.kernotec.driverscheduleservice.jpa.enums.PersonTypeEnum;
import com.kernotec.driverscheduleservice.jpa.service.PersonService;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthUtil {

    private final PersonService personService;

    public UUID getUserIdFromAuthentication(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken authenticationJwt) {
            return UUID.fromString(authenticationJwt.getToken()
                .getSubject());
        }

        return null;
    }

    public UUID getPersonIdFromAuthenticationThrow(Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);

        if (userId == null) {
            throw new PersonException("user.id.is.null", "", HttpStatus.BAD_REQUEST.value());
        }

        return personService.findByUserIdThrow(userId)
            .getId();
    }

    public Collection<String> getRolesFromAuthentication(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken authenticationJwt) {
            return authenticationJwt.getToken()
                .getClaimAsStringList("roles");
        }

        return List.of();
    }

    public boolean userContainsRole(Authentication authentication, PersonTypeEnum role) {
        Collection<String> roles = getRolesFromAuthentication(authentication);

        return roles.contains(String.format("ROLE_%s", role));
    }
}
