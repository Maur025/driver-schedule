package com.kernotec.driverschedule.service.jpa.specification.trip.criteria;

import com.kernotec.driverschedule.service.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverschedule.service.jpa.specification.common.criteria.CriteriaDate;
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
