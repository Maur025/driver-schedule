package com.kernotec.driverscheduleservice.request.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.request.jpa.entity.TransportationRequest;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationRequestRepository extends
    BaseRepository<TransportationRequest, UUID>
{

}
