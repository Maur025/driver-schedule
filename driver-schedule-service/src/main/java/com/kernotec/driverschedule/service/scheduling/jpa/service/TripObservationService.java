package com.kernotec.driverschedule.service.scheduling.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripObservation;
import com.kernotec.driverschedule.service.scheduling.jpa.repository.TripObservationRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TripObservationService extends BaseServiceImpl<TripObservation, UUID> {

    private final TripObservationRepository repository;

    @Override
    protected String resourceName() {
        return "Trip Observation";
    }

    @Override
    protected BaseRepository<TripObservation, UUID> repository() {
        return repository;
    }
}
