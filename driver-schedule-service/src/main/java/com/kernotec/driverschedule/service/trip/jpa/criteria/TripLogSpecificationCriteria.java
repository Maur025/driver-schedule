package com.kernotec.driverschedule.service.trip.jpa.criteria;

import com.kernotec.driverschedule.service.trip.jpa.enums.TripStateEnum;
import com.kernotec.driverschedule.service.common.criteria.CriteriaDate;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripLogSpecificationCriteria extends CriteriaDate {

    private UUID tripId;

    private Set<TripStateEnum> tripStates;
}
