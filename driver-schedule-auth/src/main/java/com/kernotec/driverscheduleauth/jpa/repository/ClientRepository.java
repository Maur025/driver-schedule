package com.kernotec.driverscheduleauth.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleauth.jpa.entity.Client;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends BaseRepository<Client, UUID> {

    Optional<Client> findByClientId(String clientId);
}
