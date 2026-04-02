package com.kernotec.driverscheduleauth.security.grants;

import com.kernotec.driverscheduleauth.jpa.enums.LoginProtocolParams;
import com.kernotec.driverscheduleauth.jpa.service.ClientService;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class GrantHandlerCommon {

    private final ClientService clientService;

    public String getClientIdOfParams(MultiValueMap<String, String> params) {
        return getParamOfParams(LoginProtocolParams.CLIENT_ID_PARAM, params);
    }

    public String getUsernameOfParams(MultiValueMap<String, String> params) {
        return getParamOfParams(LoginProtocolParams.USERNAME_PARAM, params);
    }

    public String getPasswordOfParams(MultiValueMap<String, String> params) {
        return getParamOfParams(LoginProtocolParams.PASSWORD_PARAM, params);
    }

    public String getRefreshTokenOfParams(MultiValueMap<String, String> params) {
        return getParamOfParams(LoginProtocolParams.REFRESH_TOKEN_PARAM, params);
    }

    public String getSubjectTokenOfParams(MultiValueMap<String, String> params) {
        return getParamOfParams(LoginProtocolParams.SUBJECT_TOKEN_PARAM, params);
    }

    public String getSubjectTokenTypeOfParams(MultiValueMap<String, String> params) {
        return getParamOfParams(LoginProtocolParams.SUBJECT_TOKEN_TYPE_PARAM, params);
    }

    public String getScopeOfParams(MultiValueMap<String, String> params) {
        return getParamOfParams(LoginProtocolParams.SCOPE_PARAM, params);
    }

    public String getRealmOfParams(MultiValueMap<String, String> params) {
        return getParamOfParams(LoginProtocolParams.REALM_PARAM, params);
    }

    private String getParamOfParams(LoginProtocolParams param, MultiValueMap<String, String> params)
    {
        return params.getFirst(param.getValue());
    }

    public void validateClientId(UUID realmId, String clientId) {
        if (clientId == null || clientId.isBlank()) {
            log.debug("client id is null or blank, nothing to process");
            return;
        }

        clientService.findByRealmIdAndClientIdThrow(realmId, clientId);
    }

    public Date getExpirationTime(Long tokenExp) {
        return new Date(System.currentTimeMillis() + tokenExp);
    }
}
