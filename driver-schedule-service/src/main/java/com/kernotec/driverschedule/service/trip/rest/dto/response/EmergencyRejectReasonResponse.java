package com.kernotec.driverschedule.service.trip.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverschedule.resource.rest.dto.response.ReasonResponse;
import com.kernotec.driverschedule.resource.rest.dto.response.WithReason;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class EmergencyRejectReasonResponse extends EntityResponse implements WithReason {

    private String otherReason;

    private UUID reasonId;
    private ReasonResponse reason;

    private UUID tripEmergencyId;
    private TripEmergencyResponse tripEmergency;
}
