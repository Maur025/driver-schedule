package com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.observation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.observation.ObservationResponse;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.TripResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TripObservationResponse extends EntityResponse {

    private String otherObservation;

    private UUID tripId;
    private TripResponse trip;

    private UUID observationId;
    private ObservationResponse observation;
}
