package com.kernotec.driverschedule.service.schedule.jpa.criteria;

import com.kernotec.driverschedule.service.schedule.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverschedule.service.trip.jpa.enums.TripStateEnum;
import com.kernotec.driverschedule.common.dto.CriteriaDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleTransportationSpecificationCriteria extends CriteriaDate {

    private ZonedDateTime conflictValidationFrom;
    private ZonedDateTime conflictValidationTo;
    private UUID vehicleId;
    private UUID driverId;
    private UUID transportationRequestId;
    private UUID scheduleTransportationExcludeId;
    private ScheduleTransportationStateEnum scheduleTransportationState;
    private Collection<ScheduleTransportationStateEnum> scheduleTransportationStates;

    private UUID personRequestedId;

    private Collection<UUID> vehicleIds;
    private Collection<UUID> driverIds;

    private String keyword;

    private ZonedDateTime greaterThanOrEqualDate;

    private Collection<TripStateEnum> tripStates;
}
