package com.kernotec.driverscheduleservice.jpa.service.request;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.request.TransportationRequestLog;
import com.kernotec.driverscheduleservice.jpa.repository.request.TransportationRequestLogRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TransportationRequestLogService extends
    BaseServiceImpl<TransportationRequestLog, UUID>
{

    private final TransportationRequestLogRepository repository;

    @Override
    protected String resourceName() {
        return "Transportation Request Log";
    }

    @Override
    protected BaseRepository<TransportationRequestLog, UUID> repository() {
        return repository;
    }
}
