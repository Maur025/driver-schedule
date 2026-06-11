package com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverschedule.common.audit.user.dto.response.AuditEntityUserResponse;
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
public class EmergencyReasonResponse extends AuditEntityUserResponse implements WithReason {

    private String otherReason;

    private UUID reasonId;
    private ReasonResponse reason;

    private UUID tripEmergencyId;
    private TripEmergencyResponse tripEmergency;
}
