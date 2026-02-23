package com.kernotec.driverscheduleauth.security.grants;

import com.kernotec.driverscheduleauth.jpa.enums.GrantTypeEnum;
import com.kernotec.driverscheduleauth.jpa.enums.LoginProtocolParams;
import com.kernotec.driverscheduleauth.rest.command.AuthLoginWithPasswordCmd;
import com.kernotec.driverscheduleauth.rest.command.AuthLoginWithPasswordCmd.Request;
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

    private final AuthLoginWithPasswordCmd authLoginWithPasswordCmd;

    @Override
    public GrantTypeEnum getGrantType() {
        return GrantTypeEnum.PASSWORD;
    }

    @Override
    public OpenIdConnectTokenResponse handle(UUID realmId, MultiValueMap<String, String> params) {
        log.info("UN SALUDO DESDE EL HANDLER DE PASSWORD");

        return authLoginWithPasswordCmd.withRequest(Request.builder()
                .grantPasswordCredentialsRequest(GrantPasswordCredentialsRequest.builder()
                    .username(params.getFirst(LoginProtocolParams.USERNAME.getValue()))
                    .password(params.getFirst(LoginProtocolParams.PASSWORD.getValue()))
                    .build())
                .build())
            .execute();
    }
}
