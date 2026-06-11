package com.kernotec.driverschedule.service.scheduling.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.CancelReason;
import com.kernotec.driverschedule.service.scheduling.jpa.repository.CancelReasonRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CancelReasonService extends BaseServiceImpl<CancelReason, UUID> {

    private final CancelReasonRepository repository;

    @Override
    protected String resourceName() {
        return "Cancel Reason";
    }

    @Override
    protected BaseRepository<CancelReason, UUID> repository() {
        return repository;
    }
}
