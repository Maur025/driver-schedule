package com.kernotec.driverschedule.service.scheduling.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.CancelRequestReason;
import com.kernotec.driverschedule.service.scheduling.jpa.repository.CancelRequestReasonRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CancelRequestReasonService extends BaseServiceImpl<CancelRequestReason, UUID> {

    private final CancelRequestReasonRepository repository;

    @Override
    protected String resourceName() {
        return "Cancel Request Reason";
    }

    @Override
    protected BaseRepository<CancelRequestReason, UUID> repository() {
        return repository;
    }
}
