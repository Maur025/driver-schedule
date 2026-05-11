package com.kernotec.driverschedule.service.trip.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.trip.jpa.entity.EmergencyRejectReason;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyRejectReasonRepository extends
    BaseRepository<EmergencyRejectReason, UUID>
{

    List<EmergencyRejectReason> findByTripEmergencyId(UUID tripEmergencyId);
}
