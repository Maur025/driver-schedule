package com.kernotec.driverscheduleservice.rest.dto.request.request.request.coord;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class RequestCoordCreateRequest extends BaseRequest {

    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @NotNull
    private Integer index;
    private String description;
    private String locationName;
    private Double durationMinutes;
    private Double distanceKilometers;
    private Double waitTimeMinutes;
    private ZonedDateTime estimatedArrivalTime;
    private String locationDescription;
    private UUID locationId;
}
