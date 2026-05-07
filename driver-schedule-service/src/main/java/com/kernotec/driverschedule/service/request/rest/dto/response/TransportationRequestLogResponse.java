package com.kernotec.driverschedule.service.request.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverschedule.service.audit.user.dto.response.AuditEntityUserResponse;
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
