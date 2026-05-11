package com.kernotec.driverschedule.service.trip.jpa.criteria;

import com.kernotec.driverschedule.service.trip.jpa.enums.TripEmergencyStateEnum;
import com.kernotec.driverschedule.service.common.criteria.CriteriaDate;
import java.util.Collection;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripEmergencySpecificationCriteria extends CriteriaDate {

    private Collection<TripEmergencyStateEnum> tripEmergencyStates;

    private UUID personEmergencyReportedId;
    private UUID tripId;

    private String keyword;
}
