package com.kernotec.driverscheduleauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.command.TokenClaimSetGetCmd;
import com.kernotec.driverscheduleauth.command.user.UserGetDtoCmd;
import com.kernotec.driverscheduleauth.exception.TokenException;
import com.kernotec.driverscheduleauth.jpa.dto.UserDto;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectUserInfoResponse;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserInfoGetDataCmd extends
    AbstractTransactionalRequiredCommand<UserInfoGetDataCmd.Request, OpenIdConnectUserInfoResponse>
{

    private final TokenClaimSetGetCmd tokenClaimSetGetCmd;
    private final UserGetDtoCmd userGetDtoCmd;

    @Override
    protected OpenIdConnectUserInfoResponse run(Request request) {
        JWTClaimsSet jwtClaimsSet = tokenClaimSetGetCmd.withRequest(
                TokenClaimSetGetCmd.Request.builder()
                    .token(request.token)
                    .build())
            .execute();

        Date expiration = jwtClaimsSet.getExpirationTime();

        if (expiration.before(new Date())) {
            throw new TokenException("expired", "", HttpStatus.UNAUTHORIZED.value());
        }

        UserDto userDto = userGetDtoCmd.withRequest(UserGetDtoCmd.Request.builder()
                .userId(UUID.fromString(jwtClaimsSet.getSubject()))
                .build())
            .execute();

        return OpenIdConnectUserInfoResponse.builder()
            .sub(jwtClaimsSet.getSubject())
            .username(jwtClaimsSet.getClaim("preferred_username")
                .toString())
            .name(jwtClaimsSet.getClaim("name")
                .toString())
            .roles(new ArrayList<>(userDto.getRoles()))
            .build();
    }

    @Builder
    public record Request(@NotNull String token) {

    }
}
