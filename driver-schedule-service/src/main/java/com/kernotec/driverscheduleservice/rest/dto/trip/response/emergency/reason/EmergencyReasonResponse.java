package com.kernotec.driverscheduleservice.rest.dto.trip.response.emergency.reason;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.reason.ReasonResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.reason.WithReason;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.emergency.TripEmergencyResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class EmergencyReasonResponse extends EntityResponse implements WithReason {

    private String otherReason;

    private UUID reasonId;
    private ReasonResponse reason;

    private UUID tripEmergencyId;
    private TripEmergencyResponse tripEmergency;
}
