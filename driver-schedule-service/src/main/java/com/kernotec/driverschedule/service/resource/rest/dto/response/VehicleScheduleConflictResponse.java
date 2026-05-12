package com.kernotec.driverschedule.service.resource.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverschedule.service.schedule.rest.dto.response.ScheduleTransportationResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class VehicleScheduleConflictResponse extends EntityResponse {

    private boolean hasConflict;
    private List<ScheduleTransportationResponse> scheduleTransportationConflicts;
}
