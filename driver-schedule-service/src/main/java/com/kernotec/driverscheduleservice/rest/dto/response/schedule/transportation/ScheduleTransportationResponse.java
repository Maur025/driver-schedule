package com.kernotec.driverscheduleservice.rest.dto.response.schedule.transportation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverscheduleservice.audit.user.dto.response.AuditEntityUserResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.person.PersonResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.reason.ReasonResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.schedule.transportation.state.ScheduleTransportationStateResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.transportation.request.TransportationRequestResponse;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ScheduleTransportationResponse extends AuditEntityUserResponse {

    private ZonedDateTime scheduleFrom;
    private ZonedDateTime scheduleTo;
    private LocalDateTime scheduledDate;

    private UUID transportationRequestId;
    private TransportationRequestResponse transportationRequest;

    private UUID personRequestedId;
    private PersonResponse personRequested;

    private UUID scheduleTransportationStateId;
    private ScheduleTransportationStateResponse scheduleTransportationState;

    private Set<ReasonResponse> cancelReasons;
    private Set<ReasonResponse> rescheduleReasons;
}
