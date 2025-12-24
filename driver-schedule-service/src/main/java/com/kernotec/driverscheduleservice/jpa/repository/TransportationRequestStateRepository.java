package com.kernotec.driverscheduleservice.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequestState;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationRequestStateRepository extends
    BaseRepository<TransportationRequestState, UUID>
{

    Optional<TransportationRequestState> findByCode(String code);
}
