package com.kernotec.driverscheduleservice.request.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.driverscheduleservice.request.jpa.enums.TripTypeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class TransportationRequestCreateRequest extends BaseRequest {

    @NotNull
    private String peopleNumber;
    private String assets;
    private String passengers;
    @NotNull
    private ZonedDateTime startTime;
    @NotNull
    private ZonedDateTime endTime;
    @NotNull
    private ZonedDateTime requestedDate;
    @NotNull
    private TripTypeEnum tripType;
    private Boolean isShortNotice;
    private String detail;
    private Boolean isAssetPickup;
    private Double estimatedTotalDistanceKm;
    private Double estimatedTotalDurationMin;
    private UUID personRequestedId;

    private String zoneId;

    @NotNull
    private List<@Valid RequestCoordCreateRequest> requestCoords;
}
