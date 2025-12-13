package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequestState;
import com.kernotec.driverscheduleservice.jpa.repository.TransportationRequestStateRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TransportationRequestStateService extends
    BaseServiceImpl<TransportationRequestState, UUID>
{

    private final TransportationRequestStateRepository repository;

    @Override
    protected String resourceName() {
        return "Transportation Request State";
    }

    @Override
    protected BaseRepository<TransportationRequestState, UUID> repository() {
        return repository;
    }
}
