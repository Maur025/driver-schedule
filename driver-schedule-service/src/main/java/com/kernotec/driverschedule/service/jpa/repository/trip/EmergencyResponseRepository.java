package com.kernotec.driverschedule.service.jpa.repository.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.jpa.entity.trip.EmergencyResponse;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyResponseRepository extends BaseRepository<EmergencyResponse, UUID> {

}
