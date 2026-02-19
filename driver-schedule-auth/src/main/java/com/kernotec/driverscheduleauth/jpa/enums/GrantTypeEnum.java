package com.kernotec.driverscheduleauth.jpa.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GrantTypeEnum {
    refresh_token, password, @JsonProperty(
        "urn:ietf:params:oauth:grant-type:token-exchange") token_exchange;
}
