package com.kernotec.driverschedule.service.scheduling.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.scheduling.exception.TripEmergencyStateException;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripEmergencyState;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.TripEmergencyStateEnum;
import com.kernotec.driverschedule.service.scheduling.jpa.repository.TripEmergencyStateRepository;
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
