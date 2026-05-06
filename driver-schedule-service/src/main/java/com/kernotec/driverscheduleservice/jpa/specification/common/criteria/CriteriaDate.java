package com.kernotec.driverscheduleservice.jpa.specification.common.criteria;

import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriteriaDate {

    private ZonedDateTime simpleDate;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private ZonedDateTime monthDate;
    private ZonedDateTime yearDate;

    private String zoneId;
}
