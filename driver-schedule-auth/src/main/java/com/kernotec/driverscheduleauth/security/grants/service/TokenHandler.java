package com.kernotec.driverscheduleauth.security.grants.service;

public interface TokenHandler<T, C> {

    C getTokenClaimsSetWithValidation(String tokenStr);

    C getTokenClaimsSet(String tokenStr);

    C getTokenClaimsSet(T signedToken);

    T getSignedJwt(String tokenStr);

    void validateToken(String tokenStr);

    void verifyToken(T signedToken);

    void verifyClaims(T signedJWT);
}
