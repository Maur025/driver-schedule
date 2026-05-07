package com.kernotec.driverschedule.service.jpa.specification.schedule.criteria;

import com.kernotec.driverschedule.service.jpa.enums.schedule.ScheduleTransportationStateEnum;
import com.kernotec.driverschedule.service.jpa.enums.schedule.TripAssignmentStateCodeEnum;
import com.kernotec.driverschedule.service.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverschedule.service.jpa.specification.common.criteria.CriteriaDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripAssignmentSpecificationCriteria extends CriteriaDate {

    private UUID driverId;
    private UUID vehicleId;

    private Collection<ScheduleTransportationStateEnum> scheduleTransportationStates;

    private Collection<TripStateEnum> tripStates;
    private Collection<TripStateEnum> existingTripStates;

    private Collection<UUID> vehicleIds;
    private Collection<UUID> driverIds;

    private ZonedDateTime availableFrom;
    private ZonedDateTime availableTo;

    private Collection<TripAssignmentStateCodeEnum> tripAssignmentStates;

    private ZonedDateTime greaterThanOrEqualDate;
    private UUID scheduleTransportationExcludeId;
}
