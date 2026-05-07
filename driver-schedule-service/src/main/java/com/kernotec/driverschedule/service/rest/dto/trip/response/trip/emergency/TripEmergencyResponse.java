package com.kernotec.driverschedule.service.rest.dto.trip.response.trip.emergency;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverschedule.common.audit.user.dto.response.AuditEntityUserResponse;
import com.kernotec.driverschedule.person.rest.dto.response.PersonResponse;
import com.kernotec.driverschedule.service.rest.dto.schedule.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverschedule.service.rest.dto.trip.response.emergency.reason.EmergencyReasonResponse;
import com.kernotec.driverschedule.service.rest.dto.trip.response.emergency.reject.reason.EmergencyRejectReasonResponse;
import com.kernotec.driverschedule.service.rest.dto.trip.response.emergency.response.EmergencyResponseResponse;
import com.kernotec.driverschedule.service.rest.dto.trip.response.trip.TripResponse;
import com.kernotec.driverschedule.service.rest.dto.trip.response.trip.emergency.state.TripEmergencyStateResponse;
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
