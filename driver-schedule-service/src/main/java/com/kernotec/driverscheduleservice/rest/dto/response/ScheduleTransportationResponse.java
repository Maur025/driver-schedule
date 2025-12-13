package com.kernotec.driverscheduleservice.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ScheduleTransportationResponse extends EntityResponse {

    private ZonedDateTime scheduleFrom;
    private ZonedDateTime scheduleTo;

    private UUID vehicleId;
    private VehicleResponse vehicle;

    private UUID driverId;
    private PersonResponse driver;

    private UUID transportationRequestId;
    private TransportationRequestResponse transportationRequest;

    private UUID scheduleTransportationStateId;
    private ScheduleTransportationStateResponse scheduleTransportationState;

    private Set<ReasonResponse> cancelReasons;
    private Set<ReasonResponse> rescheduleReasons;
}
