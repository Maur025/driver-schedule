package com.kernotec.driverscheduleservice.rest.dto.request.transportation.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.driverscheduleservice.jpa.enums.TripTypeEnum;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TransportationRequestFilterRequest extends BaseRequest {

    private UUID transportationRequestStateId;
    private UUID personRequestedId;
    private TripTypeEnum tripType;
}
