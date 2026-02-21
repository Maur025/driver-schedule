package com.kernotec.driverscheduleauth.security.grants;

import com.kernotec.driverscheduleauth.exception.GrantHandlerException;
import com.kernotec.driverscheduleauth.jpa.enums.GrantTypeEnum;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GrantHandlerFactory {

    private final Map<GrantTypeEnum, GrantHandler> handlers;

    public GrantHandlerFactory(List<GrantHandler> handlerList) {
        this.handlers = handlerList.stream()
            .collect(Collectors.toMap(GrantHandler::getGrantType, handler -> handler));
    }

    public GrantHandler getHandler(GrantTypeEnum grantType) {
        return Optional.ofNullable(handlers.get(grantType))
            .orElseThrow(
                () -> new GrantHandlerException("No handler found for grant type: " + grantType));
    }
}
