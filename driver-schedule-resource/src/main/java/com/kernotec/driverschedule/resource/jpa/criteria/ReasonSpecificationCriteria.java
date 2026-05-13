package com.kernotec.driverschedule.resource.jpa.criteria;

import com.kernotec.driverschedule.resource.jpa.enums.RequestStateCode;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReasonSpecificationCriteria {

    private UUID transportationRequestId;
    private RequestStateCode transportationRequestStateCode;
}
