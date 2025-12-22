package com.kernotec.driverscheduleservice.jpa.specification.criteria;

import com.kernotec.driverscheduleservice.jpa.enums.TripTypeEnum;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportationRequestSpecificationCriteria {

    private UUID transportationRequestStateId;
    private UUID personRequestedId;
    private TripTypeEnum tripType;
    private String userId;
}
