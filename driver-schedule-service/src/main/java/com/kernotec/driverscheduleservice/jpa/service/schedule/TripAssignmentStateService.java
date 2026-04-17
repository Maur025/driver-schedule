package com.kernotec.driverscheduleservice.jpa.service.schedule;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.exception.schedule.TripAssignmentStateException;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.TripAssignmentState;
import com.kernotec.driverscheduleservice.jpa.enums.schedule.TripAssignmentStateCodeEnum;
import com.kernotec.driverscheduleservice.jpa.repository.schedule.TripAssignmentStateRepository;
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
