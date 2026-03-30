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
public class TokenUpdateCmd extends
    AbstractTransactionalRequiredCommand<TokenUpdateCmd.Request, Void>
{

    private final TokenService tokenService;

    @Override
    protected Void run(Request request) {
        Token token = tokenService.findByIdThrow(request.tokenDbId);

        if (request.tokenParentId != null) {
            token.setTokenParentId(request.tokenParentId);
        }

        if (request.issuedAt != null) {
            token.setIssuedAt(request.issuedAt);
        }

        if (request.tokenState != null) {
            token.setState(request.tokenState);
        }

        tokenService.save(token);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID tokenDbId, UUID tokenParentId, ZonedDateTime issuedAt,
                          TokenStateEnum tokenState)
    {

    }
}
