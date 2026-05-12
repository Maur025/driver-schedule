package com.kernotec.driverschedule.service.trip.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.driverschedule.service.schedule.rest.dto.request.ScheduleAddAssignmentRequest;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TripEmergencyHandledRequest extends BaseRequest {

    private UUID emergencyResponseTypeId;
    private String detail;

    private ScheduleAddAssignmentRequest addAssignment;
}
