package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.repository.TransportationRequestRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TransportationRequestService extends BaseServiceImpl<TransportationRequest, UUID> {

    private final TransportationRequestRepository repository;

    @Override
    protected String resourceName() {
        return "Transportation Request";
    }

    @Override
    protected BaseRepository<TransportationRequest, UUID> repository() {
        return repository;
    }
}
