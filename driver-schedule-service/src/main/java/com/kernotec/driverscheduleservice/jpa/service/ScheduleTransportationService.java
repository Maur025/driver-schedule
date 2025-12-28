package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.repository.ScheduleTransportationRepository;
import com.kernotec.driverscheduleservice.jpa.specification.schedule.transportation.ScheduleTransportationSpecification;
import java.time.ZonedDateTime;
import java.util.List;
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

    public List<ScheduleTransportation> findConflictByVehicleId(UUID vehicleId, ZonedDateTime from,
        ZonedDateTime to)
    {
        return repository.findAll(ScheduleTransportationSpecification.builder()
            .withAvailabilityValidation(from, to)
            .withVehicleId(vehicleId));
    }

    public List<ScheduleTransportation> findConflictByDriverId(UUID driverId, ZonedDateTime from,
        ZonedDateTime to)
    {
        return repository.findAll(ScheduleTransportationSpecification.builder()
            .withAvailabilityValidation(from, to)
            .withDriverId(driverId));
    }
}
