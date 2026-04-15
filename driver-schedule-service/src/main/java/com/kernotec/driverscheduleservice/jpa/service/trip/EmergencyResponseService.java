package com.kernotec.driverscheduleservice.jpa.service.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.trip.EmergencyResponse;
import com.kernotec.driverscheduleservice.jpa.repository.trip.EmergencyResponseRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmergencyResponseService extends BaseServiceImpl<EmergencyResponse, UUID> {

    private final EmergencyResponseRepository repository;

    @Override
    protected String resourceName() {
        return "Emergency Response";
    }

    @Override
    protected BaseRepository<EmergencyResponse, UUID> repository() {
        return repository;
    }
}
