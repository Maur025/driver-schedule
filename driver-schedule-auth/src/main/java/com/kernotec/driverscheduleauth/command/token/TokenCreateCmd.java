package com.kernotec.driverscheduleauth.command.token;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.jpa.entity.Token;
import com.kernotec.driverscheduleauth.jpa.enums.TokenStateEnum;
import com.kernotec.driverscheduleauth.jpa.service.TokenService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenCreateCmd extends
    AbstractTransactionalRequiredCommand<TokenCreateCmd.Request, UUID>
{

    private final TokenService tokenService;

    @Override
    protected UUID run(Request request) {
        var token = new Token();

        token.setTokenId(request.tokenId);
        token.setClientId(request.clientId);
        token.setToken(request.token);
        token.setTokenParentId(request.tokenParentId);
        token.setIssuedAt(request.issuedAt);
        token.setExpiresAt(request.expiresAt);
        token.setExpiresIn(request.expiresIn);
        token.setState(request.tokenState);
        token.setUserId(request.userId);

        token = tokenService.save(token);
        return token.getId();
    }

    @Builder
    public record Request(@NotNull UUID tokenId, String clientId, @NotNull String token,
                          UUID tokenParentId, @NotNull ZonedDateTime issuedAt,
                          @NotNull ZonedDateTime expiresAt, @NotNull Long expiresIn,
                          @NotNull TokenStateEnum tokenState, @NotNull UUID userId)
    {

    }
}
