package com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.emergency;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@JsonInclude(Include.NON_NULL)
public class TripEmergencyRequest extends BaseRequest {

    private Double latitude;
    private Double longitude;

    private UUID reasonId;
    private String otherReason;
}
