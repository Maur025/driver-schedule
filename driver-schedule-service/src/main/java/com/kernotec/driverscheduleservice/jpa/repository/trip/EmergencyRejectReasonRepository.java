package com.kernotec.driverscheduleservice.jpa.repository.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.trip.EmergencyRejectReason;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyRejectReasonRepository extends
    BaseRepository<EmergencyRejectReason, UUID>
{

    List<EmergencyRejectReason> findByTripEmergencyId(UUID tripEmergencyId);
}
