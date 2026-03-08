package com.kernotec.driverscheduleauth.security.grants;

import com.kernotec.driverscheduleauth.jpa.enums.GrantTypeEnum;
import com.kernotec.driverscheduleauth.jpa.enums.LoginProtocolParams;
import com.kernotec.driverscheduleauth.jpa.service.ClientService;
import com.kernotec.driverscheduleauth.rest.command.RefreshTokenGrantCmd;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectTokenResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class RefreshTokenGrantTypeHandler implements GrantHandler {

    private final RefreshTokenGrantCmd refreshTokenGrantCmd;
    private final ClientService clientService;

    @Override
    public GrantTypeEnum getGrantType() {
        return GrantTypeEnum.REFRESH_TOKEN;
    }

    @Override
    public OpenIdConnectTokenResponse handle(UUID realmId, MultiValueMap<String, String> params) {
        String clientId = params.getFirst(LoginProtocolParams.CLIENT_ID_PARAM.getValue());

        clientService.findByRealmIdAndClientIdThrow(realmId, clientId);

        return refreshTokenGrantCmd.withRequest(RefreshTokenGrantCmd.Request.builder()
                .refreshToken(params.getFirst(LoginProtocolParams.REFRESH_TOKEN_PARAM.getValue()))
                .clientId(clientId)
                .build())
            .execute();
    }
}
