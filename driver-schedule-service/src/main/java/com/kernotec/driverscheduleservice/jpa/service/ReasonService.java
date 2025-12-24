package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.Reason;
import com.kernotec.driverscheduleservice.jpa.repository.ReasonRepository;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReasonService extends BaseServiceImpl<Reason, UUID> {

    private final ReasonRepository repository;

    @Override
    protected String resourceName() {
        return "Reason";
    }

    @Override
    protected BaseRepository<Reason, UUID> repository() {
        return repository;
    }

    public Set<Reason> findRejectByTransportationRequestId(UUID transportationRequestId) {
        return repository.findRejectByTransportationRequestId(transportationRequestId);
    }
}
