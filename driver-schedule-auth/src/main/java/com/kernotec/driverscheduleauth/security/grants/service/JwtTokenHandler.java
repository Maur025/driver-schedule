package com.kernotec.driverscheduleauth.security.grants.service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public interface JwtTokenHandler extends TokenHandler<SignedJWT, JWTClaimsSet> {

}
