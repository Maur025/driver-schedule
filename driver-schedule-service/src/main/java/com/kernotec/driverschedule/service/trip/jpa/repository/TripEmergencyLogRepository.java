package com.kernotec.driverschedule.service.trip.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.trip.jpa.entity.TripEmergencyLog;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TripEmergencyLogRepository extends BaseRepository<TripEmergencyLog, UUID> {

}
