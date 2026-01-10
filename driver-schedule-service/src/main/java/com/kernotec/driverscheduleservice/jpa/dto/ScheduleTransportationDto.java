package com.kernotec.driverscheduleservice.jpa.dto;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleTransportationDto extends AuditEntityDto {

    private ZonedDateTime scheduleFrom;
    private ZonedDateTime scheduleTo;
    private LocalDateTime scheduledDate;

    private UUID vehicleId;
    private VehicleDto vehicle;

    private UUID driverId;
    private PersonDto driver;

    private UUID transportationRequestId;
    private TransportationRequestDto transportationRequest;

    private UUID personRequestedId;
    private PersonDto personRequested;

    private UUID scheduleTransportationStateId;
    private ScheduleTransportationStateDto scheduleTransportationState;

    private Set<ReasonDto> cancelReasons;
    private Set<ReasonDto> rescheduleReasons;
}
