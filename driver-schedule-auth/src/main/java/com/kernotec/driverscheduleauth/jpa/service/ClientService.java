package com.kernotec.driverscheduleauth.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleauth.exception.ClientException;
import com.kernotec.driverscheduleauth.jpa.entity.Client;
import com.kernotec.driverscheduleauth.jpa.repository.ClientRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ClientService extends BaseServiceImpl<Client, UUID> {

    private final ClientRepository repository;

    @Override
    protected String resourceName() {
        return "Client";
    }

    @Override
    protected BaseRepository<Client, UUID> repository() {
        return repository;
    }

    public Optional<Client> findByClientId(String clientId) {
        return repository.findByClientId(clientId);
    }

    public Client findByClientIdThrow(String clientId) {
        return findByClientId(clientId).orElseThrow(
            () -> new ClientException("not.found", clientId, HttpStatus.NOT_FOUND.value()));
    }
}
