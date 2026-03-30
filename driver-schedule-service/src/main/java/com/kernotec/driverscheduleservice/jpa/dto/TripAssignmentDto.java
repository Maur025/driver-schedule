package com.kernotec.driverscheduleservice.jpa.dto;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripAssignmentDto extends AuditEntityDto {

    private UUID vehicleId;
    private VehicleDto vehicle;

    private UUID driverId;
    private PersonDto driver;

    private UUID scheduleTransportationId;
    private ScheduleTransportationDto scheduleTransportation;
}
