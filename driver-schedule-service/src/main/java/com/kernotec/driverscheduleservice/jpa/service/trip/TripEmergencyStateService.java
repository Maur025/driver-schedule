package com.kernotec.driverscheduleservice.jpa.service.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.trip.TripEmergencyState;
import com.kernotec.driverscheduleservice.jpa.repository.trip.TripEmergencyStateRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TripEmergencyStateService extends BaseServiceImpl<TripEmergencyState, UUID> {

    private final TripEmergencyStateRepository repository;

    @Override
    protected String resourceName() {
        return "Trip Emergency State";
    }

    @Override
    protected BaseRepository<TripEmergencyState, UUID> repository() {
        return repository;
    }
}
