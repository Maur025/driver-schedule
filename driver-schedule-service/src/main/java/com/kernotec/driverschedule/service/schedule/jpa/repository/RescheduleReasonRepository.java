package com.kernotec.driverschedule.service.schedule.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.schedule.jpa.entity.RescheduleReason;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface RescheduleReasonRepository extends BaseRepository<RescheduleReason, UUID> {

}
