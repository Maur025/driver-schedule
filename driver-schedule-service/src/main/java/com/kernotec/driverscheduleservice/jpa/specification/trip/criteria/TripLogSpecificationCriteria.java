package com.kernotec.driverscheduleservice.jpa.specification.trip.criteria;

import com.kernotec.driverscheduleservice.jpa.enums.trip.TripStateEnum;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripLogSpecificationCriteria {

    private UUID tripId;

    private Set<TripStateEnum> tripStates;

    private ZonedDateTime simpleDate;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private ZonedDateTime monthDate;
    private ZonedDateTime yearDate;

    private String zoneId;
}
