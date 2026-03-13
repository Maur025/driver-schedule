package com.kernotec.driverscheduleauth.security.grants;

import com.kernotec.driverscheduleauth.jpa.enums.GrantTypeEnum;
import com.kernotec.driverscheduleauth.jpa.enums.SubjectTokenTypeEnum;
import com.kernotec.driverscheduleauth.rest.command.ExchangeTokenGrantCmd;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectTokenResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@RequiredArgsConstructor
@Component
public class ExchangeTokenGrantTypeHandler implements GrantHandler {

    private final GrantHandlerCommon grantHandlerCommon;
    private final ExchangeTokenGrantCmd exchangeTokenGrantCmd;

    @Override
    public GrantTypeEnum getGrantType() {
        return GrantTypeEnum.TOKEN_EXCHANGE_GRANT_TYPE;
    }

    @Override
    public OpenIdConnectTokenResponse handle(UUID realmId, MultiValueMap<String, String> params) {
        String clientId = grantHandlerCommon.getClientIdOfParams(params);

        grantHandlerCommon.validateClientId(realmId, clientId);

        return exchangeTokenGrantCmd.withRequest(ExchangeTokenGrantCmd.Request.builder()
                .subjectToken(grantHandlerCommon.getSubjectTokenOfParams(params))
                .subjectTokenType(SubjectTokenTypeEnum.fromValue(
                    grantHandlerCommon.getSubjectTokenTypeOfParams(params)))
                .scope(grantHandlerCommon.getScopeOfParams(params))
                .clientId(clientId)
                .build())
            .execute();
    }
}
