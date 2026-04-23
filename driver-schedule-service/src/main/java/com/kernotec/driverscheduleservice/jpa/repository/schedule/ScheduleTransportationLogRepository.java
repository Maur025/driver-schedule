package com.kernotec.driverscheduleservice.jpa.repository.schedule;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportationLog;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleTransportationLogRepository extends
    BaseRepository<ScheduleTransportationLog, UUID>
{

}
