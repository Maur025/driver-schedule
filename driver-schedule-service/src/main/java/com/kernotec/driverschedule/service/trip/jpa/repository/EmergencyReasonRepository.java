package com.kernotec.driverschedule.service.trip.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.trip.jpa.entity.EmergencyReason;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyReasonRepository extends BaseRepository<EmergencyReason, UUID> {

    List<EmergencyReason> findByTripEmergencyId(UUID tripEmergencyId);
}
