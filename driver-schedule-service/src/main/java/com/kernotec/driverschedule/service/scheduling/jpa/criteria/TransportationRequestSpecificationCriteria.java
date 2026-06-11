package com.kernotec.driverschedule.service.scheduling.jpa.criteria;

import com.kernotec.driverschedule.service.scheduling.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.TripTypeEnum;
import com.kernotec.driverschedule.common.dto.CriteriaDate;
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
