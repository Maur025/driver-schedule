package com.kernotec.driverscheduleservice.rest.dto.response.cancel.request.reason;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.reason.ReasonResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.transportation.request.TransportationRequestResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class CancelRequestReasonResponse extends EntityResponse {

    private String otherReason;

    private UUID reasonId;
    private ReasonResponse reason;

    private UUID transportationRequestId;
    private TransportationRequestResponse transportationRequest;
}
