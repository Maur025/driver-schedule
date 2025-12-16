package com.kernotec.driverscheduleauth.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.constraints.NotNull;
import java.text.ParseException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenClaimSetGetCmd extends
    AbstractTransactionalRequiredCommand<TokenClaimSetGetCmd.Request, JWTClaimsSet>
{

    @Override
    protected JWTClaimsSet run(Request request) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(request.token);
            return signedJWT.getJWTClaimsSet();
        } catch (ParseException ex) {
            log.error("Error parsing token", ex);
            throw new RuntimeException(ex);
        }
    }

    @Builder
    public record Request(@NotNull String token) {

    }
}
