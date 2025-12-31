package com.kernotec.driverscheduleservice.rest.dto.request.schedule.transportation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
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
    private ZonedDateTime requestedDate;
    @NotNull
    private UUID transportationRequestId;
    @NotNull
    private UUID vehicleId;
    @NotNull
    private UUID driverId;
    private String zoneId;
}
