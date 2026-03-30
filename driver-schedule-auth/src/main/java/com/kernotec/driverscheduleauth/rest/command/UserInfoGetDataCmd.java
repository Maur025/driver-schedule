package com.kernotec.driverscheduleauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.command.user.UserGetDtoCmd;
import com.kernotec.driverscheduleauth.jpa.dto.UserDto;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectUserInfoResponse;
import com.kernotec.driverscheduleauth.security.grants.TokenClaim;
import com.kernotec.driverscheduleauth.security.grants.service.JwtTokenHandler;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserInfoGetDataCmd extends
    AbstractTransactionalRequiredCommand<UserInfoGetDataCmd.Request, OpenIdConnectUserInfoResponse>
{

    private final UserGetDtoCmd userGetDtoCmd;
    private final JwtTokenHandler jwtTokenHandler;

    @Override
    protected OpenIdConnectUserInfoResponse run(Request request) {
        JWTClaimsSet jwtClaimsSet = jwtTokenHandler.getTokenClaimsSetWithValidation(request.token);

        UserDto userDto = userGetDtoCmd.withRequest(UserGetDtoCmd.Request.builder()
                .userId(UUID.fromString(jwtClaimsSet.getSubject()))
                .build())
            .execute();

        return OpenIdConnectUserInfoResponse.builder()
            .sub(jwtClaimsSet.getSubject())
            .username(jwtClaimsSet.getClaim(TokenClaim.PREFERRED_USERNAME)
                .toString())
            .name(jwtClaimsSet.getClaim(TokenClaim.FULL_NAME)
                .toString())
            .roles(new ArrayList<>(userDto.getRoles()))
            .build();
    }

    @Builder
    public record Request(@NotNull String token) {

    }
}
