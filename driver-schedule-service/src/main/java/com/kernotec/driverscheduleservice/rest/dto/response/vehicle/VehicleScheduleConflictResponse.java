package com.kernotec.driverscheduleservice.rest.dto.response.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.schedule.transportation.ScheduleTransportationResponse;
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
