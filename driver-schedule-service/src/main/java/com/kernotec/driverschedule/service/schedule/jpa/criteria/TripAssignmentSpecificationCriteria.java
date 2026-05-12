package com.kernotec.driverschedule.service.schedule.jpa.criteria;

import com.kernotec.driverschedule.service.schedule.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverschedule.service.schedule.jpa.enums.TripAssignmentStateCodeEnum;
import com.kernotec.driverschedule.service.trip.jpa.enums.TripStateEnum;
import com.kernotec.driverschedule.common.dto.CriteriaDate;
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
