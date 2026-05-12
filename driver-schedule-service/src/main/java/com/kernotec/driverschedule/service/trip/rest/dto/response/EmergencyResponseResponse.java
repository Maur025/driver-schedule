package com.kernotec.driverschedule.service.trip.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class EmergencyResponseResponse extends EntityResponse {

    private String otherResponseDetail;
    private String detail;

    private UUID tripEmergencyId;
    private TripEmergencyResponse tripEmergency;

    private UUID emergencyResponseTypeId;
    private EmergencyResponseTypeResponse emergencyResponseType;
}
