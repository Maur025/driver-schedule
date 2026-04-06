package com.kernotec.driverscheduleservice.rest.dto.trip.request.trip;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TripFinalizeRequest extends BaseRequest {

    private ZonedDateTime tripStart;
    private ZonedDateTime tripEnd;

    private Double durationTotalMinutes;
    private Double onRouteTimeMinutes;
    private Double waitTimeMinutes;

    private Double latitude;
    private Double longitude;
    private String description;

    private String zoneId;
}
