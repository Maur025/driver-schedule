package com.kernotec.driverschedule.service.rest.dto.resource.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverschedule.service.rest.dto.schedule.response.trip.assignment.TripAssignmentResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class AvailabilityForAssignmentResponse extends EntityResponse {

    private boolean hasConflict;
    private List<TripAssignmentResponse> availabilityConflicts;

    private long scheduleTransportationCount;
    private String conflictReason;
}
