package com.kernotec.driverscheduleservice.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverscheduleservice.jpa.enums.TripTypeEnum;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TransportationRequestResponse extends EntityResponse {

    private List<Double> startingCoordinate;
    private List<Double> endingCoordinate;
    private String peopleNumber;
    private String assets;
    private String passengers;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private ZonedDateTime requestedDate;
    private TripTypeEnum tripType;

    private UUID transportationRequestStateId;
    private TransportationRequestStateResponse transportationRequestState;

    private Set<ReasonResponse> rejectReasons;


}
