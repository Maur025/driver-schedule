package com.kernotec.driverschedule.service.jpa.dto.trip;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.driverschedule.person.jpa.dto.PersonDto;
import com.kernotec.driverschedule.service.jpa.dto.schedule.ScheduleTransportationDto;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripEmergencyDto extends AuditEntityDto {

    private UUID personEmergencyReportedId;
    private PersonDto personEmergencyReported;

    private UUID tripId;
    private TripDto trip;

    private UUID scheduleTransportationId;
    private ScheduleTransportationDto scheduleTransportation;

    private UUID tripEmergencyStateId;
    private TripEmergencyStateDto tripEmergencyState;
}
