package com.kernotec.driverscheduleservice.rest.dto.schedule.request.schedule.transportation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.driverscheduleservice.rest.dto.schedule.request.trip.assignment.TripAssignmentCreateRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class ScheduleTransportationCreateRequest extends BaseRequest {

    @NotNull
    private ZonedDateTime requestedStartTime;
    @NotNull
    private ZonedDateTime requestedEndTime;
    @NotNull
    private LocalDateTime requestedDate;
    @NotNull
    private UUID transportationRequestId;
    private String zoneId;

    @NotNull
    @NotEmpty
    private List<TripAssignmentCreateRequest> tripAssignments;
}
