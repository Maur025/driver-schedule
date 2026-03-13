package com.kernotec.driverscheduleauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.command.TokenSignCmd;
import com.kernotec.driverscheduleauth.command.token.TokenCreateCmd;
import com.kernotec.driverscheduleauth.command.token.TokenUpdateCmd;
import com.kernotec.driverscheduleauth.config.AuthConfigProperties;
import com.kernotec.driverscheduleauth.exception.TokenException;
import com.kernotec.driverscheduleauth.jpa.entity.Token;
import com.kernotec.driverscheduleauth.jpa.enums.TokenStateEnum;
import com.kernotec.driverscheduleauth.jpa.enums.TokenTypeEnum;
import com.kernotec.driverscheduleauth.jpa.service.TokenService;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectTokenResponse;
import com.kernotec.driverscheduleauth.security.grants.GrantHandlerCommon;
import com.kernotec.driverscheduleauth.security.grants.service.JwtTokenHandler;
import com.kernotec.driverscheduleauth.util.CommonUtil;
import com.kernotec.driverscheduleauth.util.TimeMeasureUtil;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenGrantCmd extends
    AbstractTransactionalRequiredCommand<RefreshTokenGrantCmd.Request, OpenIdConnectTokenResponse>
{

    private final AuthConfigProperties authConfigProperties;

    private final TokenService tokenService;

    private final TokenSignCmd tokenSignCmd;
    private final TokenCreateCmd tokenCreateCmd;
    private final TokenUpdateCmd tokenUpdateCmd;
    private final JwtTokenHandler jwtTokenHandler;
    private final GrantHandlerCommon grantHandlerCommon;

    @Override
    protected OpenIdConnectTokenResponse run(Request request) {
        JWTClaimsSet refreshTokenJwtClaimsSet = jwtTokenHandler.getTokenClaimsSetWithValidation(
            request.refreshToken);

        Token refreshToken = getValidRefreshToken(refreshTokenJwtClaimsSet, request);

        long accessExp = TimeMeasureUtil.getMillisecondsByTypeTime(
            authConfigProperties.getAccessTokenExp(), authConfigProperties.getAccessTokenExpType());

        JWTClaimsSet.Builder accessTokenBuilder = new JWTClaimsSet.Builder();
        refreshTokenJwtClaimsSet.getClaims()
            .forEach(accessTokenBuilder::claim);
        accessTokenBuilder.jwtID(String.valueOf(UUID.randomUUID()));
        accessTokenBuilder.expirationTime(grantHandlerCommon.getExpirationTime(accessExp));
        accessTokenBuilder.issueTime(new Date());
        accessTokenBuilder.notBeforeTime(new Date());
        JWTClaimsSet claimsSetOfAccessToken = accessTokenBuilder.build();

        String accessToken = tokenSignCmd.withRequest(TokenSignCmd.Request.builder()
                .claimsSet(claimsSetOfAccessToken)
                .build())
            .execute();

        JWTClaimsSet claimsSetOfRefreshToken = getNewRefreshTokenClaimsSet(
            refreshTokenJwtClaimsSet);

        String newRefreshToken = tokenSignCmd.withRequest(TokenSignCmd.Request.builder()
                .claimsSet(claimsSetOfRefreshToken)
                .build())
            .execute();

        long newRefreshTokenExpIn = refreshTokenJwtClaimsSet.getExpirationTime()
            .getTime() - claimsSetOfRefreshToken.getIssueTime()
            .getTime();

        tokenCreateCmd.withRequest(TokenCreateCmd.Request.builder()
                .tokenId(CommonUtil.getUuidOfString(claimsSetOfRefreshToken.getJWTID()))
                .clientId(request.clientId)
                .token(newRefreshToken)
                .issuedAt(ZonedDateTime.now())
                .expiresAt(refreshToken.getExpiresAt())
                .expiresIn(TimeMeasureUtil.getSecondsOfMilliseconds(newRefreshTokenExpIn))
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

        return OpenIdConnectTokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(newRefreshToken)
            .tokenType(TokenTypeEnum.bearer)
            .expiresIn(TimeMeasureUtil.getSecondsOfMilliseconds(accessExp))
            .refreshExpiresIn(TimeMeasureUtil.getSecondsOfMilliseconds(newRefreshTokenExpIn))
            .scope(claimsSetOfAccessToken.getClaim("scope")
                .toString())
            .build();
    }

    private Token getValidRefreshToken(JWTClaimsSet refreshTokenJwtClaimsSet, Request request) {
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

        return refreshToken;
    }

    private JWTClaimsSet getNewRefreshTokenClaimsSet(JWTClaimsSet oldRefreshTokenClaimsSet) {
        UUID newRefreshTokenId = UUID.randomUUID();

        JWTClaimsSet.Builder refreshTokenBuilder = new JWTClaimsSet.Builder();

        oldRefreshTokenClaimsSet.getClaims()
            .forEach(refreshTokenBuilder::claim);

        refreshTokenBuilder.jwtID(newRefreshTokenId.toString());
        refreshTokenBuilder.issueTime(new Date());

        return refreshTokenBuilder.build();
    }

    @Builder
    public record Request(@NotNull @NotBlank String refreshToken, String clientId) {

    }
}
