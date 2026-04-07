package com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.emergency.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.emergency.TripEmergencyResponse;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.emergency.state.TripEmergencyStateResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TripEmergencyLogResponse extends EntityResponse {

    private UUID tripEmergencyId;
    private TripEmergencyResponse tripEmergency;

    private UUID tripEmergencyStateId;
    private TripEmergencyStateResponse tripEmergencyState;
}
