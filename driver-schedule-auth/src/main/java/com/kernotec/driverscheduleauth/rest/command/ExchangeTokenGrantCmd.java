package com.kernotec.driverscheduleauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.command.TokenSignCmd;
import com.kernotec.driverscheduleauth.config.AuthConfigProperties;
import com.kernotec.driverscheduleauth.exception.TokenException;
import com.kernotec.driverscheduleauth.jpa.entity.Role;
import com.kernotec.driverscheduleauth.jpa.entity.Scope;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.enums.SubjectTokenTypeEnum;
import com.kernotec.driverscheduleauth.jpa.enums.TokenTypeEnum;
import com.kernotec.driverscheduleauth.jpa.enums.UserRoleAndPermissionEnum;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectTokenResponse;
import com.kernotec.driverscheduleauth.security.grants.GrantHandlerCommon;
import com.kernotec.driverscheduleauth.security.grants.TokenClaim;
import com.kernotec.driverscheduleauth.security.grants.service.JwtTokenHandler;
import com.kernotec.driverscheduleauth.util.TimeMeasureUtil;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.text.ParseException;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExchangeTokenGrantCmd extends
    AbstractTransactionalRequiredCommand<ExchangeTokenGrantCmd.Request, OpenIdConnectTokenResponse>
{

    private final AuthConfigProperties authConfigProperties;
    private final JwtTokenHandler jwtTokenHandler;
    private final GrantHandlerCommon grantHandlerCommon;
    private final TokenSignCmd tokenSignCmd;
    private final UserService userService;

    @Override
    protected OpenIdConnectTokenResponse run(Request request) {
        if (!SubjectTokenTypeEnum.ACCESS_TOKEN.equals(request.subjectTokenType)) {
            log.warn("Subject token type not supported: {}", request.subjectTokenType);
            return null;
        }

        JWTClaimsSet accessTokenClaimsSet = jwtTokenHandler.getTokenClaimsSetWithValidation(
            request.subjectToken);

        if (accessTokenClaimsSet.getClaim(TokenClaim.ACTOR) != null) {
            throw new TokenException("invalid", "", HttpStatus.UNAUTHORIZED.value());
        }

        Map<UserRoleAndPermissionEnum, Set<String>> rolesAndScopesMap = getRolesAndScopes(
            request.scope);

        Set<String> requestedRoles = rolesAndScopesMap.get(UserRoleAndPermissionEnum.ROLES);
        Set<String> requestedScopes = rolesAndScopesMap.get(UserRoleAndPermissionEnum.SCOPES);

        Set<String> roles = validateRequestedRoles(requestedRoles, accessTokenClaimsSet);
        Set<String> validScopes = getValidScopes(
            requestedScopes, roles, accessTokenClaimsSet.getSubject());

        String scope = String.join(" ", validScopes);

        long exchangeExp = TimeMeasureUtil.getMillisecondsByTypeTime(
            authConfigProperties.getAccessTokenExp(), authConfigProperties.getAccessTokenExpType());

        JWTClaimsSet.Builder exchangeTokenBuilder = new JWTClaimsSet.Builder();

        accessTokenClaimsSet.getClaims()
            .forEach(exchangeTokenBuilder::claim);

        exchangeTokenBuilder.jwtID(String.valueOf(UUID.randomUUID()));
        exchangeTokenBuilder.expirationTime(grantHandlerCommon.getExpirationTime(exchangeExp));
        exchangeTokenBuilder.issueTime(new Date());
        exchangeTokenBuilder.notBeforeTime(new Date());
        exchangeTokenBuilder.claim(TokenClaim.ROLES, roles);
        exchangeTokenBuilder.claim(TokenClaim.SCOPE, scope);

        exchangeTokenBuilder.claim(TokenClaim.ACTOR, getActorClaim(accessTokenClaimsSet));

        JWTClaimsSet claimsSetOfExchangeToken = exchangeTokenBuilder.build();

        String exchangeToken = tokenSignCmd.withRequest(TokenSignCmd.Request.builder()
                .claimsSet(claimsSetOfExchangeToken)
                .build())
            .execute();

        return OpenIdConnectTokenResponse.builder()
            .accessToken(exchangeToken)
            .issuedTokenType(SubjectTokenTypeEnum.ACCESS_TOKEN.getValue())
            .tokenType(TokenTypeEnum.bearer)
            .expiresIn(TimeMeasureUtil.getSecondsOfMilliseconds(exchangeExp))
            .scope(scope)
            .build();
    }

    private Map<UserRoleAndPermissionEnum, Set<String>> getRolesAndScopes(String scopes)
    {
        Set<String> scopeSet = new HashSet<>();
        Set<String> roleSet = new HashSet<>();

        List<String> scopePayloadList = List.of(scopes.split(" "));

        for (String value : scopePayloadList) {
            if (value.startsWith("ROLE_")) {
                roleSet.add(value);
                continue;
            }

            scopeSet.add(value);
        }

        return Map.of(
            UserRoleAndPermissionEnum.ROLES, roleSet, UserRoleAndPermissionEnum.SCOPES, scopeSet);
    }

    private Set<String> validateRequestedRoles(Set<String> requestedRoles,
        JWTClaimsSet jwtClaimsSet)
    {
        List<String> currentRoles;

        try {
            currentRoles = jwtClaimsSet.getListClaim(TokenClaim.ROLES)
                .stream()
                .map(String::valueOf)
                .toList();
        } catch (ParseException ex) {
            log.error("Error parsing roles claim from token claims set", ex);
            throw new RuntimeException(ex);
        }

        if (currentRoles.isEmpty() && !requestedRoles.isEmpty()) {
            throw new TokenException("role.scope.invalid", "", HttpStatus.FORBIDDEN.value());
        }

        if (!currentRoles.isEmpty() && requestedRoles.isEmpty()) {
            return new HashSet<>(currentRoles);
        }

        Set<String> currentRoleSet = new HashSet<>(currentRoles);

        if (!currentRoleSet.containsAll(requestedRoles)) {
            throw new TokenException("role.scope.invalid", "", HttpStatus.FORBIDDEN.value());
        }

        return requestedRoles;
    }

    private Set<String> getValidScopes(Set<String> requestScopes, Set<String> roles,
        String userIdStr)
    {
        if (requestScopes.isEmpty() && roles.isEmpty()) {
            return Set.of();
        }

        if (!requestScopes.isEmpty() && roles.isEmpty()) {
            throw new TokenException("role.scope.invalid", "", HttpStatus.FORBIDDEN.value());
        }

        User user = userService.findByIdThrow(UUID.fromString(userIdStr));

        Set<String> scopes = new HashSet<>();

        for (Role role : user.getRoles()) {
            if (!roles.contains("ROLE_" + role.getName())) {
                continue;
            }

            scopes.addAll(role.getScopes()
                .stream()
                .map(Scope::getName)
                .collect(Collectors.toSet()));
        }

        if (requestScopes.isEmpty()) {
            return scopes;
        }

        if (!scopes.containsAll(requestScopes)) {
            throw new TokenException("role.scope.invalid", "", HttpStatus.FORBIDDEN.value());
        }

        return requestScopes;
    }

    private Map<String, Object> getActorClaim(JWTClaimsSet jwtClaimsSet) {
        return Map.of(
            TokenClaim.SUB, jwtClaimsSet.getSubject(), TokenClaim.JTI, jwtClaimsSet.getJWTID(),
            TokenClaim.ROLES, jwtClaimsSet.getClaim(TokenClaim.ROLES)
        );
    }

    @Builder
    public record Request(@NotNull @NotBlank String subjectToken,
                          @NotNull SubjectTokenTypeEnum subjectTokenType,
                          @NotNull @NotBlank String scope, String clientId)
    {

    }
}
