package com.kernotec.driverscheduleauth.jpa.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SubjectTokenTypeEnum {

    ACCESS_TOKEN("urn:ietf:params:oauth:token-type:access_token"),
    REFRESH_TOKEN("urn:ietf:params:oauth:token-type:refresh_token"),
    ID_TOKEN("urn:ietf:params:oauth:token-type:id_token"),
    JWT("urn:ietf:params:oauth:token-type:jwt");

    private final String value;

    public static SubjectTokenTypeEnum fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (SubjectTokenTypeEnum entry : values()) {
            if (entry.value.equalsIgnoreCase(value)) {
                return entry;
            }
        }

        return null;
    }
}
