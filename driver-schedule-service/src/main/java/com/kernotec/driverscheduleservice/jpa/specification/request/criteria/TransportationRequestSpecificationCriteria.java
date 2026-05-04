package com.kernotec.driverscheduleservice.jpa.specification.request.criteria;

import com.kernotec.driverscheduleservice.jpa.enums.request.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.request.TripTypeEnum;
import com.kernotec.driverscheduleservice.jpa.specification.common.criteria.CriteriaDate;
import java.util.Collection;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportationRequestSpecificationCriteria extends CriteriaDate {

    private UUID transportationRequestStateId;
    private TransportationRequestStateEnum transportationRequestState;
    private UUID personRequestedId;
    private TripTypeEnum tripType;
    private String userId;
    private UUID onlyRecordsOfPersonId;

    private String keyword;

    private Collection<TransportationRequestStateEnum> transportationRequestStates;
}
