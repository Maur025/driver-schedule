package com.kernotec.driverschedule.service.schedule.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.schedule.jpa.entity.ScheduleTransportationLog;
import com.kernotec.driverschedule.service.schedule.jpa.repository.ScheduleTransportationLogRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ScheduleTransportationLogService extends
    BaseServiceImpl<ScheduleTransportationLog, UUID>
{

    private final ScheduleTransportationLogRepository repository;

    @Override
    protected String resourceName() {
        return "Schedule Transportation Log";
    }

    @Override
    protected BaseRepository<ScheduleTransportationLog, UUID> repository() {
        return repository;
    }
}
