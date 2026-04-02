package com.kernotec.driverscheduleauth.security.grants;

import com.kernotec.driverscheduleauth.jpa.enums.GrantTypeEnum;
import com.kernotec.driverscheduleauth.rest.command.ResourceOwnerPasswordCredentialsCmd;
import com.kernotec.driverscheduleauth.rest.dto.request.GrantPasswordCredentialsRequest;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectTokenResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceOwnerPasswordCredentialsGrantTypeHandler implements GrantHandler {

    private final ResourceOwnerPasswordCredentialsCmd resourceOwnerPasswordCredentialsCmd;
    private final GrantHandlerCommon grantHandlerCommon;

    @Override
    public GrantTypeEnum getGrantType() {
        return GrantTypeEnum.PASSWORD;
    }

    @Override
    public OpenIdConnectTokenResponse handle(UUID realmId, MultiValueMap<String, String> params) {
        String clientId = grantHandlerCommon.getClientIdOfParams(params);

        grantHandlerCommon.validateClientId(realmId, clientId);

        return resourceOwnerPasswordCredentialsCmd.withRequest(
                ResourceOwnerPasswordCredentialsCmd.Request.builder()
                    .grantPasswordCredentialsRequest(GrantPasswordCredentialsRequest.builder()
                        .username(grantHandlerCommon.getUsernameOfParams(params))
                        .password(grantHandlerCommon.getPasswordOfParams(params))
                        .clientId(clientId)
                        .realm(grantHandlerCommon.getRealmOfParams(params))
                        .build())
                    .build())
            .execute();
    }
}
