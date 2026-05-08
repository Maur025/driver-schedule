package com.kernotec.driverschedule.service.jpa.specification.trip.criteria;

import com.kernotec.driverschedule.service.jpa.enums.trip.TripEmergencyStateEnum;
import com.kernotec.driverschedule.service.jpa.specification.common.criteria.CriteriaDate;
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
