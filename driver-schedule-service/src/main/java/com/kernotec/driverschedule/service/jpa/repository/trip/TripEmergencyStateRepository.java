package com.kernotec.driverschedule.service.jpa.repository.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.jpa.entity.trip.TripEmergencyState;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TripEmergencyStateRepository extends BaseRepository<TripEmergencyState, UUID> {

    Optional<TripEmergencyState> findByCode(String code);
}
