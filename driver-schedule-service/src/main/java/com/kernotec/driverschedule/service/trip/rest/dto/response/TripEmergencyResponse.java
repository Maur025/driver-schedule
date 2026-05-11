package com.kernotec.driverschedule.service.trip.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverschedule.common.audit.user.dto.response.AuditEntityUserResponse;
import com.kernotec.driverschedule.person.rest.dto.response.PersonResponse;
import com.kernotec.driverschedule.service.schedule.rest.dto.response.ScheduleTransportationResponse;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TripEmergencyResponse extends AuditEntityUserResponse {

    private UUID personEmergencyReportedId;
    private PersonResponse personEmergencyReported;

    private UUID tripId;
    private TripResponse trip;

    private UUID scheduleTransportationId;
    private ScheduleTransportationResponse scheduleTransportation;

    private UUID tripEmergencyStateId;
    private TripEmergencyStateResponse tripEmergencyState;

    private List<EmergencyReasonResponse> emergencyReasons;
    private List<EmergencyRejectReasonResponse> emergencyRejectReasons;

    private List<EmergencyResponseResponse> emergencyResponses;
}
