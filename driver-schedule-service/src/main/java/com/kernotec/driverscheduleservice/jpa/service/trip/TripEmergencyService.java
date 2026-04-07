package com.kernotec.driverscheduleservice.jpa.service.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.trip.TripEmergency;
import com.kernotec.driverscheduleservice.jpa.repository.trip.TripEmergencyRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TripEmergencyService extends BaseServiceImpl<TripEmergency, UUID> {

    private final TripEmergencyRepository repository;

    @Override
    protected String resourceName() {
        return "Trip Emergency";
    }

    @Override
    protected BaseRepository<TripEmergency, UUID> repository() {
        return repository;
    }
}
