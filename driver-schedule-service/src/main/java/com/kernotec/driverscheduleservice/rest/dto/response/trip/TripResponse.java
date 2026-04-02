package com.kernotec.driverscheduleservice.rest.dto.response.trip;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverscheduleservice.audit.user.dto.response.AuditEntityUserResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.trip.assignment.TripAssignmentResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.trip.state.TripStateResponse;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TripResponse extends AuditEntityUserResponse {

    private ZonedDateTime tripStart;
    private ZonedDateTime tripEnd;
    private Double durationTotalMinutes;
    private Double onRouteTimeMinutes;
    private Double waitTimeMinutes;
    private String description;

    private UUID tripAssignmentId;
    private TripAssignmentResponse tripAssignment;

    private UUID tripStateId;
    private TripStateResponse tripState;
}
