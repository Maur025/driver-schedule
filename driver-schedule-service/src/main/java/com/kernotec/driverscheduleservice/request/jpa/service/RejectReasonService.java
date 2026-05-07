package com.kernotec.driverscheduleservice.request.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.request.jpa.entity.RejectReason;
import com.kernotec.driverscheduleservice.request.jpa.repository.RejectReasonRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RejectReasonService extends BaseServiceImpl<RejectReason, UUID> {

    private final RejectReasonRepository repository;

    @Override
    protected String resourceName() {
        return "Reject Reason";
    }

    @Override
    protected BaseRepository<RejectReason, UUID> repository() {
        return repository;
    }
}
