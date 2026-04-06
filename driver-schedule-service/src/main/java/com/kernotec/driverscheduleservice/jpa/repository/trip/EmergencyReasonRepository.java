package com.kernotec.driverscheduleservice.jpa.repository.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleservice.jpa.entity.trip.EmergencyReason;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyReasonRepository extends BaseRepository<EmergencyReason, UUID> {

}
