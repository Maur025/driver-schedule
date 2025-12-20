package com.kernotec.driverscheduleservice.jpa.specification.criteria;

import com.kernotec.driverscheduleservice.jpa.enums.TransportationRequestStateEnum;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReasonSpecificationCriteria {

    private UUID transportationRequestId;
    private TransportationRequestStateEnum transportationRequestStateCode;
}
