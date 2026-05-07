package com.kernotec.driverschedule.service.rest.dto.schedule.request.schedule.transportation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.driverschedule.service.rest.dto.schedule.request.trip.assignment.TripAssignmentCreateRequest;
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
public class ScheduleTransportationUpdateRequest extends BaseRequest {

    @NotNull
    private ZonedDateTime requestedStartTime;
    @NotNull
    private ZonedDateTime requestedEndTime;
    @NotNull
    private LocalDateTime requestedDate;
    private String zoneId;

    @NotNull
    private UUID reasonId;
    private String otherReason;

    @NotNull
    @NotEmpty
    private List<TripAssignmentCreateRequest> tripAssignments;
}
