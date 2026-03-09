package com.kernotec.driverscheduleauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.command.TokenClaimSetGetCmd;
import com.kernotec.driverscheduleauth.command.TokenGenerateNewCmd;
import com.kernotec.driverscheduleauth.command.TokenJWTClaimSetBuildCmd;
import com.kernotec.driverscheduleauth.command.token.TokenCreateCmd;
import com.kernotec.driverscheduleauth.command.token.TokenUpdateCmd;
import com.kernotec.driverscheduleauth.config.AuthConfigProperties;
import com.kernotec.driverscheduleauth.exception.TokenException;
import com.kernotec.driverscheduleauth.jpa.entity.Token;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.enums.TokenStateEnum;
import com.kernotec.driverscheduleauth.jpa.enums.TokenTypeEnum;
import com.kernotec.driverscheduleauth.jpa.service.TokenService;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectTokenResponse;
import com.kernotec.driverscheduleauth.util.TimeMeasureUtil;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
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
    private final TokenCreateCmd tokenCreateCmd;
    private final TokenUpdateCmd tokenUpdateCmd;

    @Override
    protected OpenIdConnectTokenResponse run(Request request) {
        JWTClaimsSet refreshTokenJwtClaimsSet = tokenClaimSetGetCmd.withRequest(
                TokenClaimSetGetCmd.Request.builder()
                    .token(request.refreshToken)
                    .build())
            .execute();

        Date expiration = refreshTokenJwtClaimsSet.getExpirationTime();

        if (expiration.before(new Date())) {
            throw new TokenException("expired", "", HttpStatus.UNAUTHORIZED.value());
        }

        Token refreshToken = tokenService.findByTokenIdAndClientIdAndUserIdAndStateInThrow(
            UUID.fromString(refreshTokenJwtClaimsSet.getJWTID()), request.clientId,
            UUID.fromString(refreshTokenJwtClaimsSet.getSubject()),
            Set.of(TokenStateEnum.ACTIVE, TokenStateEnum.REPLACED, TokenStateEnum.REVOKED)
        );

        if (!refreshToken.getToken()
            .equals(request.refreshToken))
        {
            throw new TokenException("invalid", "", HttpStatus.UNAUTHORIZED.value());
        }

        if (refreshToken.getState()
            .equals(TokenStateEnum.REVOKED))
        {
            throw new TokenException("revoked", "", HttpStatus.UNAUTHORIZED.value());
        }

        if (refreshToken.getState()
            .equals(TokenStateEnum.REPLACED))
        {
            throw new TokenException("replaced", "", HttpStatus.UNAUTHORIZED.value());
        }

        User user = userService.findByIdThrow(
            UUID.fromString(refreshTokenJwtClaimsSet.getSubject()));

        long accessExp = TimeMeasureUtil.getMillisecondsByTypeTime(
            authConfigProperties.getAccessTokenExp(), authConfigProperties.getAccessTokenExpType());

        JWTClaimsSet claimsSetOfAccessToken = tokenJWTClaimSetBuildCmd.withRequest(
                TokenJWTClaimSetBuildCmd.Request.builder()
                    .user(user)
                    .tokenExp(accessExp)
                    .clientId(request.clientId)
                    .build())
            .execute();

        String accessToken = tokenGenerateNewCmd.withRequest(TokenGenerateNewCmd.Request.builder()
                .claimsSet(claimsSetOfAccessToken)
                .build())
            .execute();

        UUID newRefreshTokenId = UUID.randomUUID();

        JWTClaimsSet.Builder refreshTokenBuilder = new JWTClaimsSet.Builder();

        refreshTokenJwtClaimsSet.getClaims()
            .forEach(refreshTokenBuilder::claim);

        refreshTokenBuilder.jwtID(newRefreshTokenId.toString());
        refreshTokenBuilder.issueTime(new Date());

        JWTClaimsSet claimsSetOfRefreshToken = refreshTokenBuilder.build();

        String newRefreshToken = tokenGenerateNewCmd.withRequest(
                TokenGenerateNewCmd.Request.builder()
                    .claimsSet(claimsSetOfRefreshToken)
                    .build())
            .execute();

        long newRefreshTokenExp = refreshTokenJwtClaimsSet.getExpirationTime()
            .getTime() - claimsSetOfRefreshToken.getIssueTime()
            .getTime();

        tokenCreateCmd.withRequest(TokenCreateCmd.Request.builder()
                .tokenId(newRefreshTokenId)
                .clientId(request.clientId)
                .token(newRefreshToken)
                .issuedAt(ZonedDateTime.now())
                .expiresAt(refreshToken.getExpiresAt())
                .expiresIn(TimeMeasureUtil.getSecondsOfMilliseconds(newRefreshTokenExp))
                .tokenState(TokenStateEnum.ACTIVE)
                .userId(refreshToken.getUserId())
                .tokenParentId(refreshToken.getId())
                .build())
            .execute();

        tokenUpdateCmd.withRequest(TokenUpdateCmd.Request.builder()
                .tokenDbId(refreshToken.getId())
                .tokenState(TokenStateEnum.REPLACED)
                .build())
            .execute();

        log.info("New refresh token: {}", newRefreshToken);

        return OpenIdConnectTokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(newRefreshToken)
            .tokenType(TokenTypeEnum.bearer)
            .expiresIn(TimeMeasureUtil.getSecondsOfMilliseconds(accessExp))
            .refreshExpiresIn(TimeMeasureUtil.getSecondsOfMilliseconds(newRefreshTokenExp))
            .scope(claimsSetOfAccessToken.getClaim("scope")
                .toString())
            .build();
    }

    @Builder
    public record Request(@NotNull String refreshToken, String clientId) {

    }
}
