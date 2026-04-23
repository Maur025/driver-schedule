package com.kernotec.driverscheduleservice.rest.dto.schedule.response.trip.assignment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverscheduleservice.audit.user.dto.response.AuditEntityUserResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.person.PersonResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.vehicle.VehicleResponse;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.trip.assignment.state.TripAssignmentStateResponse;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.TripResponse;
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
