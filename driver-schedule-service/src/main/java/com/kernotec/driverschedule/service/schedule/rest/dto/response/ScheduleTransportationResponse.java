package com.kernotec.driverschedule.service.schedule.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverschedule.common.audit.user.dto.response.AuditEntityUserResponse;
import com.kernotec.driverschedule.service.request.rest.dto.response.TransportationRequestResponse;
import com.kernotec.driverschedule.person.rest.dto.response.PersonResponse;
import java.time.ZonedDateTime;
import java.util.List;
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
    private ZonedDateTime scheduledDate;

    private UUID transportationRequestId;
    private TransportationRequestResponse transportationRequest;

    private UUID personRequestedId;
    private PersonResponse personRequested;

    private UUID scheduleTransportationStateId;
    private ScheduleTransportationStateResponse scheduleTransportationState;

    private List<CancelReasonResponse> cancelReasons;
    private List<RescheduleReasonResponse> rescheduleReasons;
    private List<TripAssignmentResponse> tripAssignments;
}
