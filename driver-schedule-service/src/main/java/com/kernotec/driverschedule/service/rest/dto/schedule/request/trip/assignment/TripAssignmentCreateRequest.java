package com.kernotec.driverschedule.service.rest.dto.schedule.request.trip.assignment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TripAssignmentCreateRequest extends BaseRequest {

    private UUID tripAssignmentId;
    private UUID vehicleId;
    private UUID driverId;

    @Schema(hidden = true)
    private ZonedDateTime estimatedStartTime;
    @Schema(hidden = true)
    private ZonedDateTime estimatedEndTime;
}
