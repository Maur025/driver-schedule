package com.kernotec.driverscheduleservice.rest.dto.response.trip.assignment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.AuditEntityResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.person.PersonResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.trip.TripResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.vehicle.VehicleResponse;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TripAssignmentResponse extends AuditEntityResponse {

    private UUID vehicleId;
    private VehicleResponse vehicle;

    private UUID driverId;
    private PersonResponse driver;

    private UUID scheduleTransportationId;
    private ScheduleTransportationResponse scheduleTransportation;

    private Set<TripResponse> trips;
}
