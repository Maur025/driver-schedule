package com.kernotec.driverschedule.service.resource.jpa.criteria;

import com.kernotec.driverschedule.service.request.jpa.enums.TransportationRequestStateEnum;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReasonSpecificationCriteria {

    private UUID transportationRequestId;
    private TransportationRequestStateEnum transportationRequestStateCode;
}
