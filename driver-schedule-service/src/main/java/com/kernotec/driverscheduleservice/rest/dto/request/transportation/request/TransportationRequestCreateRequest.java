package com.kernotec.driverscheduleservice.rest.dto.request.transportation.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.driverscheduleservice.jpa.enums.TripTypeEnum;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
    private List<Double> startingCoordinates;
    @NotNull
    private List<Double> endCoordinates;
    @NotNull
    private String peopleNumber;
    @NotNull
    private ZonedDateTime startTime;
    @NotNull
    private ZonedDateTime endTime;
    @NotNull
    private LocalDateTime requestedDate;
    @NotNull
    private TripTypeEnum tripType;
    private Boolean isShortNotice;

    private String passengers;
    private String assets;

    private UUID locationStartId;
    private UUID locationEndId;
    private String zoneId;

    private UUID personRequestedId;
    private String detail;
}
