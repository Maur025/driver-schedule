package com.kernotec.driverschedule.service.trip.jpa.criteria;

import com.kernotec.driverschedule.service.trip.jpa.enums.TripStateEnum;
import com.kernotec.driverschedule.common.dto.CriteriaDate;
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
