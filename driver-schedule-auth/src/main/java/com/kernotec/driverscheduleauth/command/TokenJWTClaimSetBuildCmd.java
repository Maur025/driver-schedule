package com.kernotec.driverscheduleauth.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.config.DriverScheduleAuthProperties;
import com.kernotec.driverscheduleauth.jpa.entity.Audience;
import com.kernotec.driverscheduleauth.jpa.entity.Client;
import com.kernotec.driverscheduleauth.jpa.entity.Scope;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.service.ClientService;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenJWTClaimSetBuildCmd extends
    AbstractTransactionalRequiredCommand<TokenJWTClaimSetBuildCmd.Request, JWTClaimsSet>
{

    private final DriverScheduleAuthProperties driverScheduleAuthProperties;
    private final ClientService clientService;

    @Override
    protected JWTClaimsSet run(Request request) {
        User user = request.user;

        String hostUrl = driverScheduleAuthProperties.getServers()
            .get(0)
            .url();

        List<String> roles = new ArrayList<>();
        Set<String> scopes = new HashSet<>();

        for (var role : user.getRoles()) {
            if (role == null) {
                continue;
            }

            roles.add(role.getName());

            scopes.addAll(role.getScopes()
                .stream()
                .map(Scope::getName)
                .collect(Collectors.toSet()));
        }

        Client client = clientService.findByClientIdThrow(request.clientId);
        Set<String> clientAudiences = client.getAudiences()
            .stream()
            .map(Audience::getName)
            .collect(Collectors.toSet());

        return new JWTClaimsSet.Builder().subject(user.getId()
                .toString())
            .jwtID(request.refreshTokenId != null ? request.refreshTokenId
                : String.valueOf(UUID.randomUUID()))
            .claim("preferred_username", user.getUsername())
            .claim("name", String.format("%s %s", user.getName(), user.getLastName()))
            .claim(
                "roles", roles.stream()
                    .map(role -> "ROLE_" + role)
                    .toList()
            )
            .claim(
                "auth_time", Instant.now()
                    .getEpochSecond()
            )
            .claim("azp", client.getClientId())
            .claim("scope", scopes.isEmpty() ? "" : String.join(" ", scopes))
            .issueTime(new Date())
            .expirationTime(new Date(System.currentTimeMillis() + request.tokenExp))
            .issuer(hostUrl)
            .audience(clientAudiences.stream()
                .toList())
            .notBeforeTime(new Date())
            .build();
    }

    @Builder
    public record Request(@NotNull User user, @NotNull Long tokenExp, String refreshTokenId,
                          String clientId)
    {

    }
}
