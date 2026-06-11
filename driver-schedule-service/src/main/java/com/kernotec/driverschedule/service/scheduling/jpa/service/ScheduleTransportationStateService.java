package com.kernotec.driverschedule.service.scheduling.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.scheduling.exception.ScheduleTransportationStateException;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.ScheduleTransportationState;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverschedule.service.scheduling.jpa.repository.ScheduleTransportationStateRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ScheduleTransportationStateService extends
    BaseServiceImpl<ScheduleTransportationState, UUID>
{

    private final ScheduleTransportationStateRepository repository;

    @Override
    protected String resourceName() {
        return "Schedule Transportation State";
    }

    @Override
    protected BaseRepository<ScheduleTransportationState, UUID> repository() {
        return repository;
    }

    public Optional<ScheduleTransportationState> findByCode(ScheduleTransportationStateEnum code) {
        return repository.findByCode(String.valueOf(code));
    }

    public ScheduleTransportationState findByCodeThrow(ScheduleTransportationStateEnum code)
    {
        return findByCode(code).orElseThrow(
            () -> new ScheduleTransportationStateException(
                "code.not.found", "'" + code + "'",
                HttpStatus.NOT_FOUND.value()
            ));
    }

    public UUID findIdByCodeThrow(ScheduleTransportationStateEnum code) {
        return findByCodeThrow(code).getId();
    }
}
