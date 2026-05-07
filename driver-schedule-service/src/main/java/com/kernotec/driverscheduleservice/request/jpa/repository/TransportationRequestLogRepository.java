package com.kernotec.driverscheduleservice.request.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.request.jpa.entity.TransportationRequestLog;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationRequestLogRepository extends
    BaseRepository<TransportationRequestLog, UUID>
{

}
