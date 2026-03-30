package com.kernotec.driverscheduleservice.jpa.specification.criteria;

import com.kernotec.driverscheduleservice.jpa.enums.TripStateEnum;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripSpecificationCriteria {

    private UUID driverId;
    private UUID vehicleId;

    private Set<TripStateEnum> tripStates;

    private ZonedDateTime simpleDate;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private ZonedDateTime monthDate;
    private ZonedDateTime yearDate;

    private String zoneId;

    private Boolean deleted;
}
