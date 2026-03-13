package com.kernotec.driverscheduleauth.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.config.AuthConfigProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenSignCmd extends
    AbstractTransactionalRequiredCommand<TokenSignCmd.Request, String>
{

    private final AuthConfigProperties authConfigProperties;

    @Override
    protected String run(Request request) {
        try {
            JWSSigner signer = new MACSigner(authConfigProperties.getSecretKey()
                .getBytes());

            var signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), request.claimsSet);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException ex) {
            log.error("Error generating token: {}", ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    @Builder
    public record Request(@NotNull JWTClaimsSet claimsSet) {

    }
}
