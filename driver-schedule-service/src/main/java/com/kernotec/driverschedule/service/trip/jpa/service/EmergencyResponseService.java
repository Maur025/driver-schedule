package com.kernotec.driverschedule.service.trip.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.trip.jpa.entity.EmergencyResponse;
import com.kernotec.driverschedule.service.trip.jpa.repository.EmergencyResponseRepository;
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
