package com.kernotec.driverschedule.service.request.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.driverschedule.service.rest.dto.resource.response.location.LocationResponse;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class RequestCoordResponse extends EntityResponse {

    private Double longitude;
    private Double latitude;
    private List<Double> coordinates;

    private Integer index;
    private String description;
    private String locationName;
    private Double durationMinutes;
    private Double distanceKilometers;
    private Double waitTimeMinutes;
    private ZonedDateTime estimatedArrivalTime;
    private String locationDescription;

    private UUID transportationRequestId;
    private TransportationRequestResponse transportationRequest;

    private UUID locationId;
    private LocationResponse location;
}
