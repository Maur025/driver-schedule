package com.kernotec.driverschedule.service.scheduling.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TransportationRequestLog;
import com.kernotec.driverschedule.service.scheduling.jpa.repository.TransportationRequestLogRepository;
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
