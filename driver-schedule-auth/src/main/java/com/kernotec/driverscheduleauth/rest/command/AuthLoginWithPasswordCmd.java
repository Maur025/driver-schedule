package com.kernotec.driverscheduleauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.command.TokenGenerateNewCmd;
import com.kernotec.driverscheduleauth.command.TokenJWTClaimSetBuildCmd;
import com.kernotec.driverscheduleauth.config.AuthConfigProperties;
import com.kernotec.driverscheduleauth.exception.UserException;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.enums.TokenTypeEnum;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import com.kernotec.driverscheduleauth.rest.dto.request.OpenIdConnectTokenRequest;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectTokenResponse;
import com.kernotec.driverscheduleauth.util.TimeMeasureUtil;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotNull;
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
public class AuthLoginWithPasswordCmd extends
    AbstractTransactionalRequiredCommand<AuthLoginWithPasswordCmd.Request, OpenIdConnectTokenResponse>
{

    private final AuthConfigProperties authConfigProperties;
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final TokenJWTClaimSetBuildCmd tokenJWTClaimSetBuildCmd;
    private final TokenGenerateNewCmd tokenGenerateNewCmd;

    @Override
    protected OpenIdConnectTokenResponse run(Request request) {
        OpenIdConnectTokenRequest tokenRequest = request.tokenRequest;

        User user = userService.findByUsernameThrow(tokenRequest.getUsername());

        if (!passwordEncoder.matches(tokenRequest.getPassword(), user.getPassword())) {
            throw new UserException("login.failed", "", HttpStatus.BAD_REQUEST.value());
        }

        log.info("access exp type: {}", authConfigProperties.getAccessTokenExpType());
        long accessExp = TimeMeasureUtil.getMillisecondsByTypeTime(
            authConfigProperties.getAccessTokenExp(), authConfigProperties.getAccessTokenExpType());
        log.info("Access token exp milliseconds: {}", accessExp);

        log.info("access exp type: {}", authConfigProperties.getRefreshTokenExpType());
        long refreshExp = TimeMeasureUtil.getMillisecondsByTypeTime(
            authConfigProperties.getRefreshTokenExp(),
            authConfigProperties.getRefreshTokenExpType()
        );
        log.info("Refresh token exp milliseconds: {}", refreshExp);

        UUID refreshTokenId = UUID.randomUUID();

        JWTClaimsSet claimsSetOfAccessToken = tokenJWTClaimSetBuildCmd.withRequest(
                TokenJWTClaimSetBuildCmd.Request.builder()
                    .user(user)
                    .tokenExp(accessExp)
                    .build())
            .execute();

        JWTClaimsSet claimsSetOfRefreshToken = tokenJWTClaimSetBuildCmd.withRequest(
                TokenJWTClaimSetBuildCmd.Request.builder()
                    .user(user)
                    .tokenExp(refreshExp)
                    .refreshTokenId(refreshTokenId.toString())
                    .build())
            .execute();

        String accessToken = tokenGenerateNewCmd.withRequest(TokenGenerateNewCmd.Request.builder()
                .claimsSet(claimsSetOfAccessToken)
                .build())
            .execute();

        String refreshToken = tokenGenerateNewCmd.withRequest(TokenGenerateNewCmd.Request.builder()
                .claimsSet(claimsSetOfRefreshToken)
                .build())
            .execute();

        return OpenIdConnectTokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType(TokenTypeEnum.bearer)
            .expiresIn(accessExp / 1000)
            .refreshExpiresIn(refreshExp / 1000)
            .scope("openid profile email")
            .build();
    }

    @Builder
    public record Request(@NotNull OpenIdConnectTokenRequest tokenRequest) {

    }
}
