package com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverschedule.common.audit.user.dto.response.AuditEntityUserResponse;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TripLogResponse extends AuditEntityUserResponse {

    private Double latitude;
    private Double longitude;
    private List<Double> coordinates;

    private UUID tripId;
    private TripResponse trip;

    private UUID tripStateId;
    private TripStateResponse tripState;
}
