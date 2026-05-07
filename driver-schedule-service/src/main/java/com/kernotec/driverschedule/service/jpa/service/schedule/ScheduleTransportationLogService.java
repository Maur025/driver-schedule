package com.kernotec.driverschedule.service.jpa.service.schedule;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.jpa.entity.schedule.ScheduleTransportationLog;
import com.kernotec.driverschedule.service.jpa.repository.schedule.ScheduleTransportationLogRepository;
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
