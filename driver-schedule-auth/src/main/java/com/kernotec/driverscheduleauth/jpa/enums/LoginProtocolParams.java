package com.kernotec.driverscheduleauth.jpa.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LoginProtocolParams {
    GRANT_TYPE_PARAM("grant_type"),
    CODE_PARAM("code"),
    REDIRECT_URI_PARAM("redirect_uri"),
    CLIENT_ID_PARAM("client_id"),
    SCOPE_PARAM("scope"),
    USERNAME("username"),
    PASSWORD("password"),
    REFRESH_TOKEN("refresh_token"),
    AUDIENCE("audience"),
    SUBJECT_TOKEN("subject_token"),
    SUBJECT_TOKEN_TYPE("subject_token_type"),
    ISSUED_TOKEN_TYPE("issued_token_type");

    private final String value;

    public static LoginProtocolParams fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (LoginProtocolParams entry : values()) {
            if (entry.value.equalsIgnoreCase(value)) {
                return entry;
            }
        }

        return null;
    }
}
