package com.kernotec.driverscheduleauth.security.grants.service;

import com.kernotec.driverscheduleauth.config.AuthConfigProperties;
import com.kernotec.driverscheduleauth.exception.TokenException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import java.text.ParseException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenHandlerImpl implements JwtTokenHandler {

    private final AuthConfigProperties authConfigProperties;

    public JWTClaimsSet getTokenClaimsSet(String tokenStr) {
        SignedJWT signedJWT = getSignedJwt(tokenStr);

        return getTokenClaimsSet(signedJWT);
    }

    public void validateToken(String tokenStr) {
        if (tokenStr == null || tokenStr.isBlank()) {
            throw new TokenException("invalid", "", HttpStatus.UNAUTHORIZED.value());
        }

        SignedJWT signedJWT = getSignedJwt(tokenStr);
        verifyToken(signedJWT);
        verifyClaims(signedJWT);
    }

    public SignedJWT getSignedJwt(String tokenStr) {
        try {
            return SignedJWT.parse(tokenStr);
        } catch (ParseException ex) {
            log.error("Error parsing tokenStr:", ex);
            throw new RuntimeException(ex);
        }
    }

    public void verifyToken(SignedJWT signedJWT) {
        String secretKey = authConfigProperties.getSecretKey();

        try {
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());

            if (!signedJWT.verify(verifier)) {
                throw new TokenException("invalid", "", HttpStatus.UNAUTHORIZED.value());
            }
        } catch (JOSEException ex) {
            log.info("Error in token verify", ex);
            throw new RuntimeException(ex);
        }
    }

    public void verifyClaims(SignedJWT signedJWT) {
        Set<String> requiredClaims = Set.of("sub", "iat", "exp", "nbf");
        var claimsVerifier = new DefaultJWTClaimsVerifier<>(null, null, requiredClaims, null);

        try {
            claimsVerifier.verify(getTokenClaimsSet(signedJWT), null);
        } catch (BadJWTException ex) {
            log.error("Error in token claims verify", ex);
            throw new RuntimeException(ex);
        }
    }

    public JWTClaimsSet getTokenClaimsSet(SignedJWT signedJWT) {
        try {
            return signedJWT.getJWTClaimsSet();
        } catch (ParseException ex) {
            log.error("Error parsing token claims set:", ex);
            throw new RuntimeException(ex);
        }
    }

    public JWTClaimsSet getTokenClaimsSetWithValidation(String tokenStr) {
        validateToken(tokenStr);
        return getTokenClaimsSet(tokenStr);
    }
}
