package com.kernotec.driverscheduleauth.jpa.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GrantTypeEnum {
    REFRESH_TOKEN("refresh_token"),
    PASSWORD("password"),
    TOKEN_EXCHANGE_GRANT_TYPE("urn:ietf:params:oauth:grant-type:token-exchange");

    private final String value;

    public static GrantTypeEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (GrantTypeEnum entry : values()) {
            if (entry.value
                .equals(value))
            {
                return entry;
            }
        }

        return null;
    }
}
