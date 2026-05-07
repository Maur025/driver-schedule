package com.kernotec.driverschedule.service.jpa.repository.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.jpa.entity.trip.EmergencyRejectReason;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyRejectReasonRepository extends
    BaseRepository<EmergencyRejectReason, UUID>
{

    List<EmergencyRejectReason> findByTripEmergencyId(UUID tripEmergencyId);
}
