package com.kernotec.driverscheduleservice.rest.dto.request.response.transportation.request.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverscheduleservice.audit.user.dto.response.AuditEntityUserResponse;
import com.kernotec.driverscheduleservice.rest.dto.request.response.transportation.request.TransportationRequestResponse;
import com.kernotec.driverscheduleservice.rest.dto.request.response.transportation.request.state.TransportationRequestStateResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TransportationRequestLogResponse extends AuditEntityUserResponse {

    private UUID transportationRequestId;
    private TransportationRequestResponse transportationRequest;

    private UUID transportationRequestStateId;
    private TransportationRequestStateResponse transportationRequestState;
}
