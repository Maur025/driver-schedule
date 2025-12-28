package com.kernotec.driverscheduleservice.rest.dto.request.schedule.transportation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ScheduleTransportationUpdateRequest extends BaseRequest {

    private ZonedDateTime requestedStartTime;
    private ZonedDateTime requestedEndTime;
    private ZonedDateTime requestedDate;
    private UUID vehicleId;
    private UUID driverId;
}
