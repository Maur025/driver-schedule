package com.kernotec.driverscheduleservice.jpa.repository.request;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.request.TransportationRequest;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationRequestRepository extends
    BaseRepository<TransportationRequest, UUID>
{

}
