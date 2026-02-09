package com.kernotec.driverscheduleservice.rest.dto.response.schedule.transportation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverscheduleservice.audit.user.dto.response.AuditEntityUserResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.cancel.reason.CancelReasonResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.person.PersonResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.reschedule.reason.RescheduleReasonResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.schedule.transportation.state.ScheduleTransportationStateResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.transportation.request.TransportationRequestResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.trip.assignment.TripAssignmentResponse;
import java.time.LocalDateTime;
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
    private LocalDateTime scheduledDate;

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
