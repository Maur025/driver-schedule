package com.kernotec.driverscheduleservice.jpa.specification.trip.criteria;

import com.kernotec.driverscheduleservice.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverscheduleservice.jpa.specification.common.criteria.CriteriaDate;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripSpecificationCriteria extends CriteriaDate {

    private UUID driverId;
    private UUID vehicleId;

    private Set<TripStateEnum> tripStates;

    private Boolean deleted;
}
