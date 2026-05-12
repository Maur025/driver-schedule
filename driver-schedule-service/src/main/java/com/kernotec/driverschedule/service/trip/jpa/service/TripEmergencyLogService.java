package com.kernotec.driverschedule.service.trip.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.trip.jpa.entity.TripEmergencyLog;
import com.kernotec.driverschedule.service.trip.jpa.repository.TripEmergencyLogRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TripEmergencyLogService extends BaseServiceImpl<TripEmergencyLog, UUID> {

    private final TripEmergencyLogRepository repository;

    @Override
    protected String resourceName() {
        return "Trip Emergency Log";
    }

    @Override
    protected BaseRepository<TripEmergencyLog, UUID> repository() {
        return repository;
    }
}
