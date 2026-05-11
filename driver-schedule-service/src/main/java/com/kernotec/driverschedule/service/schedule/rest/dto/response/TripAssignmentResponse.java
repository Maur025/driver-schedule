package com.kernotec.driverschedule.service.schedule.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverschedule.common.audit.user.dto.response.AuditEntityUserResponse;
import com.kernotec.driverschedule.person.rest.dto.response.PersonResponse;
import com.kernotec.driverschedule.service.resource.rest.dto.response.VehicleResponse;
import com.kernotec.driverschedule.service.trip.rest.dto.response.TripResponse;
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
public class TripAssignmentResponse extends AuditEntityUserResponse {

    private ZonedDateTime estimatedStartTime;
    private ZonedDateTime estimatedEndTime;

    private UUID vehicleId;
    private VehicleResponse vehicle;

    private UUID driverId;
    private PersonResponse driver;

    private UUID scheduleTransportationId;
    private ScheduleTransportationResponse scheduleTransportation;

    private UUID tripAssignmentStateId;
    private TripAssignmentStateResponse tripAssignmentState;

    private Set<TripResponse> trips;
}
