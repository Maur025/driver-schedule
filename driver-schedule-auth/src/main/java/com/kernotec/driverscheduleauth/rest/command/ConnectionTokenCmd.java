package com.kernotec.driverscheduleauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.jpa.enums.GrantTypeEnum;
import com.kernotec.driverscheduleauth.rest.dto.request.OpenIdConnectTokenRequest;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectTokenResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConnectionTokenCmd extends AbstractTransactionalRequiredCommand<ConnectionTokenCmd.Request, OpenIdConnectTokenResponse> {

    @Override
    protected OpenIdConnectTokenResponse run(Request request) {
        return switch (request.grantType){
            case password -> {}
            case refresh_token -> {}
        };
    }

    @Builder
    public record Request(GrantTypeEnum grantType, OpenIdConnectTokenRequest tokenRequest,
                          String refreshToken)
    {

    }
}
