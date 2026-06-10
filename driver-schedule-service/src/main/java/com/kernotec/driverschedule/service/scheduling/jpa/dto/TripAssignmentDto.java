package com.kernotec.driverschedule.service.scheduling.jpa.dto;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.driverschedule.person.jpa.dto.PersonDto;
import com.kernotec.driverschedule.resource.jpa.dto.VehicleDto;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripAssignmentDto extends AuditEntityDto {

    private ZonedDateTime estimatedStartTime;
    private ZonedDateTime estimatedEndTime;

    private UUID vehicleId;
    private VehicleDto vehicle;

    private UUID driverId;
    private PersonDto driver;

    private UUID scheduleTransportationId;
    private ScheduleTransportationDto scheduleTransportation;

    private Set<TripDto> trips;
}
