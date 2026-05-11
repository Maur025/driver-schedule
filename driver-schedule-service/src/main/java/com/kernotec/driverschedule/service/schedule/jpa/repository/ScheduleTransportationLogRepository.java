package com.kernotec.driverschedule.service.schedule.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.schedule.jpa.entity.ScheduleTransportationLog;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleTransportationLogRepository extends
    BaseRepository<ScheduleTransportationLog, UUID>
{

}
