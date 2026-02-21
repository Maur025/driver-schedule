package com.kernotec.driverscheduleauth.security.grants;

import com.kernotec.driverscheduleauth.jpa.enums.GrantTypeEnum;
import com.kernotec.driverscheduleauth.rest.dto.response.OpenIdConnectTokenResponse;
import java.util.UUID;
import org.springframework.util.MultiValueMap;

public interface GrantHandler {

    GrantTypeEnum getGrantType();

    OpenIdConnectTokenResponse handle(UUID realmId, MultiValueMap<String, String> params);
}
