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
import java.util.Date;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenerateAccessFromRefreshTokenCmd extends
    AbstractTransactionalRequiredCommand<GenerateAccessFromRefreshTokenCmd.Request, OpenIdConnectTokenResponse>
{

    private final AuthConfigProperties authConfigProperties;
    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;
    private final UserService userService;

    private final TokenClaimSetGetCmd tokenClaimSetGetCmd;
    private final TokenJWTClaimSetBuildCmd tokenJWTClaimSetBuildCmd;
    private final TokenGenerateNewCmd tokenGenerateNewCmd;

    @Override
    protected OpenIdConnectTokenResponse run(Request request) {
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

        if (!passwordEncoder.matches(request.refreshToken, refreshToken.getTokenHash())) {
            throw new TokenException("invalid", "", HttpStatus.UNAUTHORIZED.value());
        }

        User user = userService.findByIdThrow(UUID.fromString(jwtClaimsSet.getSubject()));

        long accessExp = TimeMeasureUtil.getMillisecondsByTypeTime(
            authConfigProperties.getAccessTokenExp(), authConfigProperties.getAccessTokenExpType());

        JWTClaimsSet claimsSetOfAccessToken = tokenJWTClaimSetBuildCmd.withRequest(
                TokenJWTClaimSetBuildCmd.Request.builder()
                    .user(user)
                    .tokenExp(accessExp)
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

    @Builder
    public record Request(@NotNull String refreshToken) {

    }
}
