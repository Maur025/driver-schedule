package com.kernotec.driverscheduleauth.rest.controller;

import com.kernotec.driverscheduleauth.jpa.enums.GrantTypeEnum;
import com.kernotec.driverscheduleauth.rest.ApiSpec.OpenIdConfigurationSpec;
import com.kernotec.driverscheduleauth.rest.dto.request.OpenIdConnectTokenRequest;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = OpenIdConfigurationSpec.TAG_NAME, description = OpenIdConfigurationSpec.TAG_DESCRIPTION)
@RequestMapping(path = OpenIdConfigurationSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class OpenIdConnectController {

    @Operation(summary = "OpenID Connect Endpoint to get token")
    @PostMapping("token")
    @ResponseStatus(HttpStatus.OK)
    public OpenIdConnectTokenResponse getTokenByGrantType(
        @RequestParam("grant_type") GrantTypeEnum grantType,
        @RequestBody(required = false) OpenIdConnectTokenRequest request,
        HttpServletResponse httpServletResponse,
        @CookieValue(value = "refresh_token", required = false) String refreshToken,
        HttpServletRequest httpServletRequest)
    {
OpenIdConnectTokenResponse openIdConnectTokenResponse =
    }
}
