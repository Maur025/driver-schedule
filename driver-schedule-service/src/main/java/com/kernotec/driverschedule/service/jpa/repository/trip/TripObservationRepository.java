package com.kernotec.driverschedule.service.jpa.repository.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverschedule.service.jpa.entity.trip.TripObservation;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TripObservationRepository extends BaseRepository<TripObservation, UUID> {

}
