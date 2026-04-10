package com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.emergency;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripEmergencyStateEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TripEmergencyPatchRequest extends BaseRequest {

    private TripEmergencyStateEnum tripEmergencyState;
}
