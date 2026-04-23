package com.kernotec.driverscheduleservice.jpa.service.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.exception.trip.TripEmergencyStateException;
import com.kernotec.driverscheduleservice.jpa.entity.trip.TripEmergencyState;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripEmergencyStateEnum;
import com.kernotec.driverscheduleservice.jpa.repository.trip.TripEmergencyStateRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

    public Optional<TripEmergencyState> findByCode(TripEmergencyStateEnum code) {
        return repository.findByCode(String.valueOf(code));
    }

    public TripEmergencyState findByCodeThrow(TripEmergencyStateEnum code) {
        return findByCode(code).orElseThrow(
            () -> new TripEmergencyStateException(
                "code.not.found", "'" + code + "'", HttpStatus.NOT_FOUND.value()));
    }

    public UUID findIdByCodeThrow(TripEmergencyStateEnum code) {
        return findByCodeThrow(code).getId();
    }
}
