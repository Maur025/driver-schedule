package com.kernotec.driverscheduleauth.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.config.DriverScheduleAuthProperties;
import com.kernotec.driverscheduleauth.jpa.entity.Audience;
import com.kernotec.driverscheduleauth.jpa.entity.Client;
import com.kernotec.driverscheduleauth.jpa.entity.Scope;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.enums.UserRoleAndPermissionEnum;
import com.kernotec.driverscheduleauth.jpa.service.ClientService;
import com.kernotec.driverscheduleauth.util.CommonUtil;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

        Set<String> clientAudiences = getClientAudiences(request.clientId);

        Map<UserRoleAndPermissionEnum, Set<String>> userRoleAndPermissionEnumSetMap = getUserRolesAndPermissions(
            user);

        Set<String> roles = userRoleAndPermissionEnumSetMap.get(UserRoleAndPermissionEnum.ROLES);
        Set<String> scopes = userRoleAndPermissionEnumSetMap.get(UserRoleAndPermissionEnum.SCOPES);

        return new JWTClaimsSet.Builder().subject(CommonUtil.getStringOfUuid(user.getId()))
            .jwtID(getJwtId(request.refreshTokenId))
            .claim("preferred_username", user.getUsername())
            .claim("name", getName(user))
            .claim("roles", getRoleList(roles))
            .claim("auth_time", getAuthTimeLong())
            .claim("azp", request.clientId)
            .claim("scope", getStringOfScopes(scopes))
            .issueTime(new Date())
            .expirationTime(getExpirationTime(request.tokenExp))
            .issuer(getIssuer())
            .audience(getAudienceList(clientAudiences))
            .notBeforeTime(new Date())
            .build();
    }

    private String getIssuer() {
        return driverScheduleAuthProperties.getServers()
            .get(0)
            .url();
    }

    private Set<String> getClientAudiences(String clientId) {
        if (clientId == null) {
            return Set.of();
        }

        Client client = clientService.findByClientIdThrow(clientId);

        return client.getAudiences()
            .stream()
            .map(Audience::getName)
            .collect(Collectors.toSet());
    }

    private Map<UserRoleAndPermissionEnum, Set<String>> getUserRolesAndPermissions(User user)
    {
        Set<String> roles = new HashSet<>();
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

        return Map.of(
            UserRoleAndPermissionEnum.ROLES, roles, UserRoleAndPermissionEnum.SCOPES, scopes);
    }

    private String getJwtId(UUID refreshTokenId) {
        return refreshTokenId != null ? CommonUtil.getStringOfUuid(refreshTokenId)
            : CommonUtil.getStringOfUuid(UUID.randomUUID());
    }

    private String getName(User user) {
        return String.format("%s %s", user.getName(), user.getLastName());
    }

    private List<String> getRoleList(Set<String> roles) {
        return roles.stream()
            .map(role -> "ROLE_" + role)
            .toList();
    }

    private long getAuthTimeLong() {
        return Instant.now()
            .getEpochSecond();
    }

    private String getStringOfScopes(Set<String> scopes) {
        return scopes.isEmpty() ? "" : String.join(" ", scopes);
    }

    private Date getExpirationTime(Long tokenExpiration) {
        return new Date(System.currentTimeMillis() + tokenExpiration);
    }

    private List<String> getAudienceList(Set<String> audiences) {
        return audiences.stream()
            .toList();
    }

    @Builder
    public record Request(@NotNull User user, @NotNull Long tokenExp, UUID refreshTokenId,
                          String clientId)
    {

    }
}
