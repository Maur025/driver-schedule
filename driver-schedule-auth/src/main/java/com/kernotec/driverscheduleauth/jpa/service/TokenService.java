package com.kernotec.driverscheduleauth.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleauth.exception.TokenException;
import com.kernotec.driverscheduleauth.jpa.entity.Token;
import com.kernotec.driverscheduleauth.jpa.enums.TokenStateEnum;
import com.kernotec.driverscheduleauth.jpa.repository.TokenRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TokenService extends BaseServiceImpl<Token, UUID> {

    private final TokenRepository repository;

    @Override
    protected String resourceName() {
        return "Token";
    }

    @Override
    protected BaseRepository<Token, UUID> repository() {
        return repository;
    }

    public Optional<Token> findByTokenIdAndClientIdAndUserIdAndStateIn(UUID tokenId,
        String clientId, UUID userId, Set<TokenStateEnum> states)
    {
        return repository.findByTokenIdAndClientIdAndUserIdAndStateIn(
            tokenId, clientId, userId, states);
    }

    public Token findByTokenIdAndClientIdAndUserIdAndStateInThrow(UUID tokenId, String clientId,
        UUID userId, Set<TokenStateEnum> states)
    {
        return findByTokenIdAndClientIdAndUserIdAndStateIn(
            tokenId, clientId, userId, states).orElseThrow(
            () -> new TokenException("not.found", "", HttpStatus.UNAUTHORIZED.value()));
    }
}
