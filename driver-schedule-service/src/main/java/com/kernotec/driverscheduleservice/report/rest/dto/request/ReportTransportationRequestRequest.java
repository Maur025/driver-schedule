package com.kernotec.driverscheduleservice.report.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverscheduleservice.request.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.request.jpa.enums.TripTypeEnum;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@JsonInclude(Include.NON_NULL)
public class ReportTransportationRequestRequest extends FilterDateRequest {

    private UUID transportationRequestStateId;
    private UUID personRequestedId;
    private TransportationRequestStateEnum transportationRequestState;
    private TripTypeEnum tripType;
}
