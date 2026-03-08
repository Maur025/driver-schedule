package com.kernotec.driverscheduleauth.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleauth.jpa.entity.Client;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends BaseRepository<Client, UUID> {

    Optional<Client> findByClientId(String clientId);

    @Query(value = """
        SELECT c.*
        FROM clients c
        INNER JOIN realm_clients rc ON rc.client_id = c.id
        WHERE rc.realm_id = :realmId AND LOWER(c.client_id) = LOWER(:clientId)
        LIMIT 1
        """, nativeQuery = true)
    Optional<Client> findByRealmIdAndClientId(@Param("realmId") UUID realmId,
        @Param("clientId") String clientId);
}
