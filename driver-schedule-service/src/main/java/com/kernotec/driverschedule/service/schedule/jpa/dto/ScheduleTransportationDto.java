package com.kernotec.driverschedule.service.schedule.jpa.dto;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.driverschedule.person.jpa.dto.PersonDto;
import com.kernotec.driverschedule.resource.jpa.dto.ReasonDto;
import com.kernotec.driverschedule.service.request.jpa.dto.TransportationRequestDto;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
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

    private UUID transportationRequestId;
    private TransportationRequestDto transportationRequest;

    private UUID personRequestedId;
    private PersonDto personRequested;

    private UUID scheduleTransportationStateId;
    private ScheduleTransportationStateDto scheduleTransportationState;

    private Set<ReasonDto> cancelReasons;
    private Set<ReasonDto> rescheduleReasons;

    private List<TripAssignmentDto> tripAssignments;
}
