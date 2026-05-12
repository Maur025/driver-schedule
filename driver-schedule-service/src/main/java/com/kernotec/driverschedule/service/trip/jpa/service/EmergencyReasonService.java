package com.kernotec.driverschedule.service.trip.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.trip.jpa.entity.EmergencyReason;
import com.kernotec.driverschedule.service.trip.jpa.repository.EmergencyReasonRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmergencyReasonService extends BaseServiceImpl<EmergencyReason, UUID> {

    private final EmergencyReasonRepository repository;

    @Override
    protected String resourceName() {
        return "Emergency Reason";
    }

    @Override
    protected BaseRepository<EmergencyReason, UUID> repository() {
        return repository;
    }

    public List<EmergencyReason> findByTripEmergencyId(UUID tripEmergencyId) {
        return repository.findByTripEmergencyId(tripEmergencyId);
    }
}
