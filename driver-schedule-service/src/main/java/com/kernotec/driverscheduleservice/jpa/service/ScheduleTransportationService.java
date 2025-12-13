package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.repository.ScheduleTransportationRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ScheduleTransportationService extends BaseServiceImpl<ScheduleTransportation, UUID> {

    private final ScheduleTransportationRepository repository;

    @Override
    protected String resourceName() {
        return "Schedule Transportation";
    }

    @Override
    protected BaseRepository<ScheduleTransportation, UUID> repository() {
        return repository;
    }
}
