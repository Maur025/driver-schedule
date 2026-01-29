package com.kernotec.driverscheduleservice.rest.dto.request.schedule.transportation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.driverscheduleservice.jpa.enums.ScheduleTransportationStateEnum;
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
public class ScheduleTransportationFilterRequest extends BaseRequest {

    private UUID transportationRequestId;
    private UUID driverId;
    private UUID vehicleId;
    private ScheduleTransportationStateEnum scheduleTransportationState;
    private List<ScheduleTransportationStateEnum> scheduleTransportationStates;

    private ZonedDateTime simpleDate;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private ZonedDateTime monthDate;
    private ZonedDateTime yearDate;
    private String zoneId;
}
