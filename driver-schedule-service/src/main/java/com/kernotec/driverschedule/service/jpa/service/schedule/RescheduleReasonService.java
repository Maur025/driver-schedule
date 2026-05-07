package com.kernotec.driverschedule.service.jpa.service.schedule;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.jpa.entity.schedule.RescheduleReason;
import com.kernotec.driverschedule.service.jpa.repository.schedule.RescheduleReasonRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RescheduleReasonService extends BaseServiceImpl<RescheduleReason, UUID> {

    private final RescheduleReasonRepository repository;

    @Override
    protected String resourceName() {
        return "Reschedule Reason";
    }

    @Override
    protected BaseRepository<RescheduleReason, UUID> repository() {
        return repository;
    }
}
