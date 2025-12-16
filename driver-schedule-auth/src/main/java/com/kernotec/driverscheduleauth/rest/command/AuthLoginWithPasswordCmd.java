package com.kernotec.driverscheduleauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import com.kernotec.driverscheduleauth.rest.dto.request.OpenIdConnectTokenRequest;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectTokenResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthLoginWithPasswordCmd extends
    AbstractTransactionalRequiredCommand<AuthLoginWithPasswordCmd.Request, OpenIdConnectTokenResponse>
{

    private final UserService userService;

    @Override
    protected OpenIdConnectTokenResponse run(Request request) {
        OpenIdConnectTokenRequest tokenRequest = request.tokenRequest;

        User user = userService.find
    }

    @Builder
    public record Request(@NotNull OpenIdConnectTokenRequest tokenRequest) {

    }
}
