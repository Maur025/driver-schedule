package com.kernotec.driverscheduleservice.jpa.service.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.trip.EmergencyReason;
import com.kernotec.driverscheduleservice.jpa.repository.trip.EmergencyReasonRepository;
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
