package com.kernotec.driverscheduleauth.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.jpa.entity.Token;
import com.kernotec.driverscheduleauth.jpa.service.TokenService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenCreateCmd extends
    AbstractTransactionalRequiredCommand<TokenCreateCmd.Request, UUID>
{

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected UUID run(Request request) {
        var token = new Token();

        token.setClientId(request.clientId);
        token.setTokenHash(passwordEncoder.encode(request.tokenHash));
        token.setTokenId(request.tokenId);
        token.setIssuedAt(request.issuedAt);
        token.setExpiresAt(request.expiresAt);
        token.setExpiresIn(request.expiresIn);
        token.setRevoked(request.revoked);
        token.setUserId(request.userId);

        token = tokenService.save(token);
        return token.getId();
    }

    @Builder
    public record Request(String clientId, @NotNull String tokenHash, @NotNull UUID tokenId,
                          @NotNull ZonedDateTime issuedAt, @NotNull ZonedDateTime expiresAt,
                          @NotNull Long expiresIn, @NotNull Boolean revoked, @NotNull UUID userId)
    {

    }
}
