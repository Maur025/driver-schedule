package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportationState;
import com.kernotec.driverscheduleservice.jpa.repository.ScheduleTransportationStateRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
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
}
