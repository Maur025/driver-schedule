package com.kernotec.driverschedule.service.scheduling.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.scheduling.exception.TripAssignmentStateException;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripAssignmentState;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.TripAssignmentStateCodeEnum;
import com.kernotec.driverschedule.service.scheduling.jpa.repository.TripAssignmentStateRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TripAssignmentStateService extends BaseServiceImpl<TripAssignmentState, UUID> {

    private final TripAssignmentStateRepository repository;

    @Override
    protected String resourceName() {
        return "Trip Assignment State";
    }

    @Override
    protected BaseRepository<TripAssignmentState, UUID> repository() {
        return repository;
    }

    public Optional<TripAssignmentState> findByCode(TripAssignmentStateCodeEnum code) {
        return repository.findByCode(String.valueOf(code));
    }

    public TripAssignmentState findByCodeThrow(TripAssignmentStateCodeEnum code) {
        return findByCode(code).orElseThrow(
            () -> new TripAssignmentStateException(
                "code.not.found", "'" + code + "'",
                HttpStatus.NOT_FOUND.value()
            ));
    }

    public UUID findIdByCodeThrow(TripAssignmentStateCodeEnum code) {
        return findByCodeThrow(code).getId();
    }
}
