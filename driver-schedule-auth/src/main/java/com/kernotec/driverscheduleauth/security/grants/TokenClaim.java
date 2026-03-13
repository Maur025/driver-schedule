package com.kernotec.driverscheduleauth.security.grants;

import com.nimbusds.jwt.JWTClaimNames;

public final class TokenClaim {

    public static final String SUB = JWTClaimNames.SUBJECT;
    public static final String IAT = JWTClaimNames.ISSUED_AT;
    public static final String EXP = JWTClaimNames.EXPIRATION_TIME;
    public static final String JTI = JWTClaimNames.JWT_ID;
    public static final String AUDIENCE = JWTClaimNames.AUDIENCE;
    public static final String ISSUER = JWTClaimNames.ISSUER;
    public static final String NOT_BEFORE = JWTClaimNames.NOT_BEFORE;

    public static final String PREFERRED_USERNAME = "preferred_username";
    public static final String FULL_NAME = "name";
    public static final String ROLES = "roles";
    public static final String CLIENT_ID = "azp";
    public static final String AUTH_TIME = "auth_time";
    public static final String SCOPE = "scope";
    public static final String ACTOR = "act";

    private TokenClaim() {
    }
}
