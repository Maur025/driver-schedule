package com.kernotec.driverscheduleservice.common.security;

import com.kernotec.driverscheduleservice.jpa.enums.resource.PersonTypeEnum;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class SecurityAuthProviderContext implements SecurityAuthProvider {

    private Optional<JwtAuthenticationToken> getJwtToken() {
        return Optional.ofNullable(SecurityContextHolder.getContext()
                .getAuthentication())
            .filter(JwtAuthenticationToken.class::isInstance)
            .map(JwtAuthenticationToken.class::cast);
    }

    @Override
    public UUID getUserId() {
        return getJwtToken().map(jwtToken -> UUID.fromString(jwtToken.getToken()
                .getSubject()))
            .orElse(null);
    }

    @Override
    public Collection<String> getRoles() {
        return getJwtToken().map(jwtToken -> jwtToken.getToken()
                .getClaimAsStringList("roles"))
            .orElse(List.of());
    }

    @Override
    public boolean userContainsRole(PersonTypeEnum role) {
        Collection<String> roles = getRoles();

        return roles.contains(String.format("ROLE_%s", role));
    }

    @Override
    public Collection<String> getScopes() {
        return getJwtToken().map(jwtToken -> jwtToken.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList())
            .orElse(List.of());
    }

    @Override
    public boolean hasScope(String scope) {
        Collection<String> scopes = getScopes();

        return scopes.contains(scope);
    }

    @Override
    public boolean hasOnlyOneRole() {
        int countRole = 0;
        for (PersonTypeEnum personType : PersonTypeEnum.values()) {
            if (userContainsRole(personType)) {
                countRole++;
            }
        }

        return countRole == 1;
    }
}
