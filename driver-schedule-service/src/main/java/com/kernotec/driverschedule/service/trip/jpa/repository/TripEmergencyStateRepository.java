package com.kernotec.driverschedule.service.trip.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.trip.jpa.entity.TripEmergencyState;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TripEmergencyStateRepository extends BaseRepository<TripEmergencyState, UUID> {

    Optional<TripEmergencyState> findByCode(String code);
}
