package com.kernotec.driverscheduleauth.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleauth.jpa.entity.Token;
import com.kernotec.driverscheduleauth.jpa.enums.TokenStateEnum;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends BaseRepository<Token, UUID> {

    Optional<Token> findByTokenIdAndClientIdAndUserIdAndStateIn(UUID tokenId, String clientId,
        UUID userId, Set<TokenStateEnum> states);
}
