package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.RescheduleReason;
import com.kernotec.driverscheduleservice.jpa.repository.RescheduleReasonRepository;
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
