package com.kernotec.driverschedule.service.scheduling.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.TripTypeEnum;
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
public class TransportationRequestFilterRequest extends BaseRequest {

    private TransportationRequestStateEnum transportationRequestState;
    private UUID transportationRequestStateId;
    private UUID personRequestedId;
    private TripTypeEnum tripType;

    private ZonedDateTime simpleDate;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private ZonedDateTime monthDate;
    private ZonedDateTime yearDate;
    private String zoneId;

    private String keyword;

    private Set<TransportationRequestStateEnum> transportationRequestStates;
}
