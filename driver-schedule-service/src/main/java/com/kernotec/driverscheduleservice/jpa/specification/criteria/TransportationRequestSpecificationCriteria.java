package com.kernotec.driverscheduleservice.jpa.specification.criteria;

import com.kernotec.driverscheduleservice.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.TripTypeEnum;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportationRequestSpecificationCriteria {

    private UUID transportationRequestStateId;
    private TransportationRequestStateEnum transportationRequestState;
    private UUID personRequestedId;
    private TripTypeEnum tripType;
    private String userId;
    private String zoneId;

    private ZonedDateTime simpleDate;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private ZonedDateTime monthDate;
    private ZonedDateTime yearDate;
}
