package com.kernotec.driverschedule.service.jpa.service.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.jpa.entity.trip.EmergencyRejectReason;
import com.kernotec.driverschedule.service.jpa.repository.trip.EmergencyRejectReasonRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmergencyRejectReasonService extends BaseServiceImpl<EmergencyRejectReason, UUID> {

    private final EmergencyRejectReasonRepository repository;

    @Override
    protected String resourceName() {
        return "Emergency Reject Reason";
    }

    @Override
    protected BaseRepository<EmergencyRejectReason, UUID> repository() {
        return repository;
    }

    public List<EmergencyRejectReason> findByTripEmergencyId(UUID tripEmergencyId) {
        return repository.findByTripEmergencyId(tripEmergencyId);
    }
}
