package com.kernotec.driverscheduleauth.rest.controller;

import com.kernotec.driverscheduleauth.config.AuthConfigProperties;
import com.kernotec.driverscheduleauth.jpa.enums.GrantTypeEnum;
import com.kernotec.driverscheduleauth.jpa.enums.LoginProtocolParams;
import com.kernotec.driverscheduleauth.jpa.enums.RefreshTokenSecureEnum;
import com.kernotec.driverscheduleauth.jpa.service.RealmService;
import com.kernotec.driverscheduleauth.rest.ApiSpec.OpenIdConnectSpec;
import com.kernotec.driverscheduleauth.rest.command.UserInfoGetDataCmd;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectTokenResponse;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectUserInfoResponse;
import com.kernotec.driverscheduleauth.security.grants.GrantHandler;
import com.kernotec.driverscheduleauth.security.grants.GrantHandlerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Duration;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = OpenIdConnectSpec.TAG_NAME, description = OpenIdConnectSpec.TAG_DESCRIPTION)
@RequestMapping(path = OpenIdConnectSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class OpenIdConnectController {

    private final AuthConfigProperties authConfigProperties;
    private final UserInfoGetDataCmd userInfoGetDataCmd;
    private final RealmService realmService;
    private final GrantHandlerFactory grantHandlerFactory;

    @Operation(summary = "OpenID Connect Endpoint to get token")
    @PostMapping(value = "token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OpenIdConnectTokenResponse> processGrantRequest(
        @PathVariable String realm, @RequestParam MultiValueMap<String, String> formParameters,
        @CookieValue(value = "refresh_token", required = false) String refreshTokenFromCookie)
    {
        UUID realmId = realmService.findRealmIdByNameInCache(realm);

        String grantTypeStr = formParameters.getFirst(
            LoginProtocolParams.GRANT_TYPE_PARAM.getValue());
        var grantType = GrantTypeEnum.fromValue(grantTypeStr);

        if (refreshTokenFromCookie != null && !refreshTokenFromCookie.isBlank()) {
            formParameters.add(
                LoginProtocolParams.REFRESH_TOKEN.getValue(), refreshTokenFromCookie);
        }

        GrantHandler grantHandler = grantHandlerFactory.getHandler(grantType);
        OpenIdConnectTokenResponse openIdConnectTokenResponse = grantHandler.handle(
            realmId, formParameters);

        var responseBuilder = ResponseEntity.ok();

        if (grantType != null && grantType.equals(GrantTypeEnum.PASSWORD)
            && openIdConnectTokenResponse != null)
        {
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

            responseBuilder.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        }

        return responseBuilder.body(openIdConnectTokenResponse);
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
