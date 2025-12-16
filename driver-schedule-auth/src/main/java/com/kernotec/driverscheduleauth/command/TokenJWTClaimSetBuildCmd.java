package com.kernotec.driverscheduleauth.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.config.AuthConfigProperties;
import com.kernotec.driverscheduleauth.config.DriverScheduleAuthProperties;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenJWTClaimSetBuildCmd extends
    AbstractTransactionalRequiredCommand<TokenJWTClaimSetBuildCmd.Request, JWTClaimsSet>
{

    private final DriverScheduleAuthProperties driverScheduleAuthProperties;
    private final AuthConfigProperties authConfigProperties;

    @Override
    protected JWTClaimsSet run(Request request) {
        User user = request.user;

        String hostUrl = driverScheduleAuthProperties.getServers()
            .get(0)
            .url();

        List<String> roles = new ArrayList<>();

        for (var role : user.getRoles()) {
            roles.add(role.getName());
        }

        return new JWTClaimsSet.Builder().subject(user.getId()
                .toString())
            .jwtID(request.refreshTokenId != null ? request.refreshTokenId
                : String.valueOf(UUID.randomUUID()))
            .claim("preferred_username", user.getUsername())
            .claim("name", String.format("%s %s", user.getName(), user.getLastName()))
            .claim("realm_access", Map.of("roles", roles))
            .claim(
                "auth_time", Instant.now()
                    .getEpochSecond()
            )
            .claim("azp", "driver-schedule-frontend-app")
            .claim("scope", "openid profile email")
            .issueTime(new Date())
            .expirationTime(new Date(System.currentTimeMillis() + request.tokenExp))
            .issuer(hostUrl)
            .audience(authConfigProperties.getAudience())
            .notBeforeTime(new Date())
            .build();
    }

    @Builder
    public record Request(@NotNull User user, @NotNull Long tokenExp, String refreshTokenId) {

    }
}
