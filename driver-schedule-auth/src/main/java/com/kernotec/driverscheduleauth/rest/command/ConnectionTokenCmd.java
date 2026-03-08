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
public class ConnectionTokenCmd extends
    AbstractTransactionalRequiredCommand<ConnectionTokenCmd.Request, OpenIdConnectTokenResponse>
{

    private final ResourceOwnerPasswordCredentialsCmd resourceOwnerPasswordCredentialsCmd;
    private final RefreshTokenGrantCmd refreshTokenGrantCmd;

    @Override
    protected OpenIdConnectTokenResponse run(Request request) {
        /*return switch (request.grantType) {
            case password -> authLoginWithPasswordCmd.withRequest(
                    ResourceOwnerPasswordCredentialsCmd.Request.builder()
                        .tokenRequest(request.tokenRequest)
                        .build())
                .execute();
            case refresh_token -> generateAccessFromRefreshTokenCmd.withRequest(
                    RefreshTokenGrantCmd.Request.builder()
                        .refreshToken(request.refreshToken)
                        .build())
                .execute();
            case token_exchange -> null;
        };*/

        return null;
    }

    @Builder
    public record Request(GrantTypeEnum grantType, OpenIdConnectTokenRequest tokenRequest,
                          String refreshToken)
    {

    }
}
