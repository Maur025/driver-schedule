package com.kernotec.driverschedule.service.scheduling.rest.dto.request.trip;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TripCreateRequest extends BaseRequest {

    private UUID tripAssignmentId;

    private Double latitude;
    private Double longitude;

    private String zoneId;
}
