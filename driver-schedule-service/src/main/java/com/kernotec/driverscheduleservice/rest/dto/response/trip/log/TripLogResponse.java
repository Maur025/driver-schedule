package com.kernotec.driverscheduleservice.rest.dto.response.trip.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverscheduleservice.audit.user.dto.response.AuditEntityUserResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.trip.TripResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.trip.state.TripStateResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TripLogResponse extends AuditEntityUserResponse {

    private UUID tripId;
    private TripResponse trip;

    private UUID tripStateId;
    private TripStateResponse tripState;
}
