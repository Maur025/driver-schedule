package com.kernotec.driverscheduleauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.command.TokenClaimSetGetCmd;
import com.kernotec.driverscheduleauth.command.TokenGenerateNewCmd;
import com.kernotec.driverscheduleauth.command.TokenJWTClaimSetBuildCmd;
import com.kernotec.driverscheduleauth.config.AuthConfigProperties;
import com.kernotec.driverscheduleauth.exception.TokenException;
import com.kernotec.driverscheduleauth.jpa.entity.Token;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.enums.TokenTypeEnum;
import com.kernotec.driverscheduleauth.jpa.service.TokenService;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectTokenResponse;
import com.kernotec.driverscheduleauth.util.TimeMeasureUtil;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.Date;
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
public class RefreshTokenGrantCmd extends
    AbstractTransactionalRequiredCommand<RefreshTokenGrantCmd.Request, OpenIdConnectTokenResponse>
{

    private final AuthConfigProperties authConfigProperties;

    private final TokenService tokenService;
    private final UserService userService;

    private final TokenClaimSetGetCmd tokenClaimSetGetCmd;
    private final TokenJWTClaimSetBuildCmd tokenJWTClaimSetBuildCmd;
    private final TokenGenerateNewCmd tokenGenerateNewCmd;

    @Override
    protected OpenIdConnectTokenResponse run(Request request) {

        log.info(
            "refresh token {} request received for clientId: {}", request.refreshToken,
            request.clientId
        );

        JWTClaimsSet jwtClaimsSet = tokenClaimSetGetCmd.withRequest(
                TokenClaimSetGetCmd.Request.builder()
                    .token(request.refreshToken)
                    .build())
            .execute();

        Date expiration = jwtClaimsSet.getExpirationTime();

        if (expiration.before(new Date())) {
            throw new TokenException("expired", "", HttpStatus.UNAUTHORIZED.value());
        }

        Token refreshToken = tokenService.findByUserIdAndTokenIdAndRevokedThrow(
            UUID.fromString(jwtClaimsSet.getSubject()), UUID.fromString(jwtClaimsSet.getJWTID()),
            false
        );

        log.info("refresh token found {}", refreshToken.getToken());

        if (!refreshToken.getToken()
            .equals(request.refreshToken))
        {
            throw new TokenException("invalid", "", HttpStatus.UNAUTHORIZED.value());
        }

        User user = userService.findByIdThrow(UUID.fromString(jwtClaimsSet.getSubject()));

        long accessExp = TimeMeasureUtil.getMillisecondsByTypeTime(
            authConfigProperties.getAccessTokenExp(), authConfigProperties.getAccessTokenExpType());

        JWTClaimsSet claimsSetOfAccessToken = tokenJWTClaimSetBuildCmd.withRequest(
                TokenJWTClaimSetBuildCmd.Request.builder()
                    .user(user)
                    .tokenExp(accessExp)
                    .clientId(request.clientId)
                    .roleFilters(getRolesToFilter(jwtClaimsSet))
                    .build())
            .execute();

        String accessToken = tokenGenerateNewCmd.withRequest(TokenGenerateNewCmd.Request.builder()
                .claimsSet(claimsSetOfAccessToken)
                .build())
            .execute();

        return OpenIdConnectTokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(request.refreshToken)
            .tokenType(TokenTypeEnum.bearer)
            .expiresIn(accessExp / 1000)
            .scope("openid profile email")
            .build();
    }

    private Set<String> getRolesToFilter(JWTClaimsSet jwtClaimsSet) {
        try {
            Set<String> roles = jwtClaimsSet.getListClaim("roles")
                .stream()
                .map(String::valueOf)
                .collect(Collectors.toSet());

            if (roles.isEmpty()) {
                return Set.of();
            }

            return roles.stream()
                .map(value -> value.replace("ROLE_", ""))
                .collect(Collectors.toSet());
        } catch (ParseException e) {
            log.error("error while parsing roles from refresh token claim set", e);
            throw new RuntimeException(e);
        }
    }

    @Builder
    public record Request(@NotNull String refreshToken, String clientId) {

    }
}
