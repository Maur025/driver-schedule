package com.kernotec.driverscheduleservice.jpa.specification.criteria;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleTransportationSpecificationCriteria {

    private ZonedDateTime conflictValidationFrom;
    private ZonedDateTime conflictValidationTo;
    private UUID vehicleId;
    private UUID driverId;
}
