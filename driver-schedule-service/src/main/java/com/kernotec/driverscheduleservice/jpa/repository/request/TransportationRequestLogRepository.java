package com.kernotec.driverscheduleservice.jpa.repository.request;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.request.TransportationRequestLog;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationRequestLogRepository extends
    BaseRepository<TransportationRequestLog, UUID>
{

}
