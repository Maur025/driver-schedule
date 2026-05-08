package com.kernotec.driverschedule.service.rest.dto.trip.response.trip.emergency.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverschedule.common.audit.user.dto.response.AuditEntityUserResponse;
import com.kernotec.driverschedule.service.rest.dto.trip.response.trip.emergency.TripEmergencyResponse;
import com.kernotec.driverschedule.service.rest.dto.trip.response.trip.emergency.state.TripEmergencyStateResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TripEmergencyLogResponse extends AuditEntityUserResponse {

    private UUID tripEmergencyId;
    private TripEmergencyResponse tripEmergency;

    private UUID tripEmergencyStateId;
    private TripEmergencyStateResponse tripEmergencyState;
}
