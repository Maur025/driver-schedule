package com.kernotec.driverscheduleservice.jpa.specification.criteria;

import com.kernotec.driverscheduleservice.jpa.enums.ScheduleTransportationStateEnum;
import java.time.ZonedDateTime;
import java.util.List;
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
    private UUID transportationRequestId;
    private String zoneId;
    private UUID scheduleTransportationExcludeId;
    private ScheduleTransportationStateEnum scheduleTransportationState;
    private List<ScheduleTransportationStateEnum> scheduleTransportationStates;

    private ZonedDateTime simpleDate;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private ZonedDateTime monthDate;
    private ZonedDateTime yearDate;

    private UUID personRequestedId;

    private List<UUID> vehicleIds;
    private List<UUID> driverIds;

    private String keyword;

    private ZonedDateTime greaterThanOrEqualDate;
}
