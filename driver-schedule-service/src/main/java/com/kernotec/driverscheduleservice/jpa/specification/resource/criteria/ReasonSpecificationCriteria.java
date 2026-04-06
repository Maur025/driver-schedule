package com.kernotec.driverscheduleservice.jpa.specification.resource.criteria;

import com.kernotec.driverscheduleservice.jpa.enums.request.TransportationRequestStateEnum;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReasonSpecificationCriteria {

    private UUID transportationRequestId;
    private TransportationRequestStateEnum transportationRequestStateCode;
}
