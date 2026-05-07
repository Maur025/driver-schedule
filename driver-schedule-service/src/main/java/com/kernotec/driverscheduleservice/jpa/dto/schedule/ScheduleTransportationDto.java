package com.kernotec.driverscheduleservice.jpa.dto.schedule;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.driverscheduleservice.jpa.dto.resource.PersonDto;
import com.kernotec.driverscheduleservice.jpa.dto.resource.ReasonDto;
import com.kernotec.driverscheduleservice.request.jpa.dto.TransportationRequestDto;
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
