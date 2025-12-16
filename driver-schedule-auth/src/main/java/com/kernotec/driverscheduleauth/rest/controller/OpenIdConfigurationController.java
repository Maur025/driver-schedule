package com.kernotec.driverscheduleauth.rest.controller;

import com.kernotec.driverscheduleauth.config.DriverScheduleAuthProperties;
import com.kernotec.driverscheduleauth.rest.ApiSpec.OpenIdConfigurationSpec;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConfigurationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = OpenIdConfigurationSpec.TAG_NAME, description = OpenIdConfigurationSpec.TAG_DESCRIPTION)
@RequestMapping(path = OpenIdConfigurationSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class OpenIdConfigurationController {

    private final DriverScheduleAuthProperties driverScheduleAuthProperties;

    @Operation(summary = "Retrieve OpenID Configuration")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public OpenIdConfigurationResponse getOpenIdConfiguration() {
        String serverHost = driverScheduleAuthProperties.getServers()
            .get(0)
            .url();

        String realm = "/realms/driver-schedule-auth";

        return OpenIdConfigurationResponse.builder()
            .issuer(serverHost + realm)
            .authorizationEndpoint(serverHost + realm + "/protocol/openid-connect/auth")
            .tokenEndpoint(serverHost + realm + "/protocol/openid-connect/token")
            .userinfoEndpoint(serverHost + realm + "/protocol/openid-connect/userinfo")
            .jwksUri(serverHost + realm + "/protocol/openid-connect/certs")
            .responseTypesSupported(List.of("code", "token", "id_token"))
            .subjectTypesSupported(List.of("public"))
            .idTokenSigningAlgValuesSupported(List.of("RS256"))
            .build();
    }
}
