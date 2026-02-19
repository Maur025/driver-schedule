package com.kernotec.driverscheduleauth.rest.controller;

import com.kernotec.driverscheduleauth.config.AuthConfigProperties;
import com.kernotec.driverscheduleauth.jpa.enums.GrantTypeEnum;
import com.kernotec.driverscheduleauth.jpa.enums.RefreshTokenSecureEnum;
import com.kernotec.driverscheduleauth.jpa.service.RealmService;
import com.kernotec.driverscheduleauth.rest.ApiSpec.OpenIdConnectSpec;
import com.kernotec.driverscheduleauth.rest.command.ConnectionTokenCmd;
import com.kernotec.driverscheduleauth.rest.command.UserInfoGetDataCmd;
import com.kernotec.driverscheduleauth.rest.dto.request.OpenIdConnectTokenRequest;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectTokenResponse;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectUserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = OpenIdConnectSpec.TAG_NAME, description = OpenIdConnectSpec.TAG_DESCRIPTION)
@RequestMapping(path = OpenIdConnectSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class OpenIdConnectController {

    private final AuthConfigProperties authConfigProperties;
    private final ConnectionTokenCmd connectionTokenCmd;
    private final UserInfoGetDataCmd userInfoGetDataCmd;
    private final RealmService realmService;

    @Operation(summary = "OpenID Connect Endpoint to get token")
    @PostMapping("token")
    @ResponseStatus(HttpStatus.OK)
    public OpenIdConnectTokenResponse getTokenByGrantType(@PathVariable String realm,
        @RequestParam("grant_type") GrantTypeEnum grantType, @RequestParam String audience,
        @RequestParam String clienId,
        @RequestBody(required = false) OpenIdConnectTokenRequest request,
        HttpServletResponse httpServletResponse,
        @CookieValue(value = "refresh_token", required = false) String refreshToken)
    {
        realmService.findRealmIdByNameInCache(realm);

        OpenIdConnectTokenResponse openIdConnectTokenResponse = connectionTokenCmd.withRequest(
                ConnectionTokenCmd.Request.builder()
                    .grantType(grantType)
                    .tokenRequest(request)
                    .refreshToken(refreshToken)
                    .build())
            .execute();

        if (grantType.equals(GrantTypeEnum.password) && openIdConnectTokenResponse != null) {
            var refreshTokenCookie = ResponseCookie.from(
                    "refresh_token", openIdConnectTokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(RefreshTokenSecureEnum.prod.equals(
                    authConfigProperties.getRefreshTokenSecure()))
                .path("/")
                .maxAge(Duration.ofSeconds(openIdConnectTokenResponse.getRefreshExpiresIn()))
                .sameSite(
                    RefreshTokenSecureEnum.prod.equals(authConfigProperties.getRefreshTokenSecure())
                        ? "None" : "Lax")
                .build();

            httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        }

        return openIdConnectTokenResponse;
    }

    @Operation(summary = "OpenId Connect Endpoint to userinfo endpoint")
    @GetMapping("userinfo")
    @ResponseStatus(HttpStatus.OK)
    public OpenIdConnectUserInfoResponse getUserInfoData(@PathVariable String realm,
        @RequestHeader("Authorization") String authorizationHeader)
    {
        realmService.findRealmIdByNameInCache(realm);

        String token = authorizationHeader.substring("Bearer ".length());

        return userInfoGetDataCmd.withRequest(UserInfoGetDataCmd.Request.builder()
                .token(token)
                .build())
            .execute();
    }
}
