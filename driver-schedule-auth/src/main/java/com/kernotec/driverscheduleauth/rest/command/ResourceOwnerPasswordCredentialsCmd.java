package com.kernotec.driverscheduleauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.command.TokenSignCmd;
import com.kernotec.driverscheduleauth.command.TokenJWTClaimSetBuildCmd;
import com.kernotec.driverscheduleauth.command.token.TokenCreateCmd;
import com.kernotec.driverscheduleauth.config.AuthConfigProperties;
import com.kernotec.driverscheduleauth.exception.UserException;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.enums.TokenStateEnum;
import com.kernotec.driverscheduleauth.jpa.enums.TokenTypeEnum;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import com.kernotec.driverscheduleauth.rest.dto.request.GrantPasswordCredentialsRequest;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectTokenResponse;
import com.kernotec.driverscheduleauth.util.TimeMeasureUtil;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ResourceOwnerPasswordCredentialsCmd extends
    AbstractTransactionalRequiredCommand<ResourceOwnerPasswordCredentialsCmd.Request, OpenIdConnectTokenResponse>
{

    private final AuthConfigProperties authConfigProperties;
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final TokenJWTClaimSetBuildCmd tokenJWTClaimSetBuildCmd;
    private final TokenSignCmd tokenSignCmd;
    private final TokenCreateCmd tokenCreateCmd;

    @Override
    protected OpenIdConnectTokenResponse run(Request request) {
        GrantPasswordCredentialsRequest grantPasswordCredentialsRequest = request.grantPasswordCredentialsRequest;

        User user = userService.findByUsernameThrow(grantPasswordCredentialsRequest.getUsername());

        if (!passwordEncoder.matches(
            grantPasswordCredentialsRequest.getPassword(), user.getPassword()))
        {
            throw new UserException("login.failed", "", HttpStatus.BAD_REQUEST.value());
        }

        long accessExp = TimeMeasureUtil.getMillisecondsByTypeTime(
            authConfigProperties.getAccessTokenExp(), authConfigProperties.getAccessTokenExpType());

        long refreshExp = TimeMeasureUtil.getMillisecondsByTypeTime(
            authConfigProperties.getRefreshTokenExp(),
            authConfigProperties.getRefreshTokenExpType()
        );

        UUID refreshTokenId = UUID.randomUUID();

        JWTClaimsSet claimsSetOfAccessToken = tokenJWTClaimSetBuildCmd.withRequest(
                TokenJWTClaimSetBuildCmd.Request.builder()
                    .user(user)
                    .tokenExp(accessExp)
                    .clientId(grantPasswordCredentialsRequest.getClientId())
                    .build())
            .execute();

        JWTClaimsSet claimsSetOfRefreshToken = tokenJWTClaimSetBuildCmd.withRequest(
                TokenJWTClaimSetBuildCmd.Request.builder()
                    .user(user)
                    .tokenExp(refreshExp)
                    .refreshTokenId(refreshTokenId)
                    .clientId(grantPasswordCredentialsRequest.getClientId())
                    .build())
            .execute();

        String accessToken = tokenSignCmd.withRequest(TokenSignCmd.Request.builder()
                .claimsSet(claimsSetOfAccessToken)
                .build())
            .execute();

        String refreshToken = tokenSignCmd.withRequest(TokenSignCmd.Request.builder()
                .claimsSet(claimsSetOfRefreshToken)
                .build())
            .execute();

        tokenCreateCmd.withRequest(TokenCreateCmd.Request.builder()
                .tokenId(refreshTokenId)
                .clientId(grantPasswordCredentialsRequest.getClientId())
                .token(refreshToken)
                .issuedAt(ZonedDateTime.now())
                .expiresAt(ZonedDateTime.now()
                    .plus(Duration.ofMillis(refreshExp)))
                .expiresIn(TimeMeasureUtil.getSecondsOfMilliseconds(refreshExp))
                .tokenState(TokenStateEnum.ACTIVE)
                .userId(user.getId())
                .build())
            .execute();

        return OpenIdConnectTokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType(TokenTypeEnum.bearer)
            .expiresIn(TimeMeasureUtil.getSecondsOfMilliseconds(accessExp))
            .refreshExpiresIn(TimeMeasureUtil.getSecondsOfMilliseconds(refreshExp))
            .scope(claimsSetOfAccessToken.getClaim("scope")
                .toString())
            .build();
    }

    @Builder
    public record Request(
        @NotNull GrantPasswordCredentialsRequest grantPasswordCredentialsRequest)
    {

    }
}
