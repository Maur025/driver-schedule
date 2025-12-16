package com.kernotec.driverscheduleauth.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleauth.jpa.entity.Token;
import com.kernotec.driverscheduleauth.jpa.repository.TokenRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
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
}
