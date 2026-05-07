package com.kernotec.driverschedule.common.audit.user.listener;

import com.kernotec.driverschedule.common.audit.user.BaseAuditEntityUser;
import com.kernotec.driverschedule.common.audit.user.json.AuthUserData;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class AuditUserDataListener {

    @PrePersist
    public void onPrePersist(BaseAuditEntityUser entity) {
        AuthUserData authUserData = getAuthUserData();

        if (authUserData != null) {
            entity.setCreatedByUser(authUserData);
            entity.setUpdatedByUser(authUserData);
        }
    }

    @PreUpdate
    public void onPreUpdate(BaseAuditEntityUser entity) {
        AuthUserData authUserData = getAuthUserData();

        if (authUserData != null) {
            entity.setUpdatedByUser(authUserData);
        }
    }

    private AuthUserData getAuthUserData() {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return getUserData(jwtAuthenticationToken);
        }

        return null;
    }

    private AuthUserData getUserData(JwtAuthenticationToken jwtAuthenticationToken) {
        Jwt token = jwtAuthenticationToken.getToken();

        return AuthUserData.builder()
            .id(token.getClaim("sub"))
            .username(token.getClaim("preferred_username"))
            .name(token.getClaim("name"))
            .roles(token.getClaimAsStringList("roles"))
            .authTime(token.getClaim("auth_time"))
            .build();
    }
}
