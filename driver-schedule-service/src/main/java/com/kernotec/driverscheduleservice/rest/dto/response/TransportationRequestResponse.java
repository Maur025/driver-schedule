package com.kernotec.driverscheduleservice.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.AuditEntityResponse;
import com.kernotec.driverscheduleservice.jpa.enums.TripTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TransportationRequestResponse extends AuditEntityResponse {

    @Schema(description = "coordinate tuple in geoJson format [longitude, latitude]")
    private List<Double> startingCoordinates;
    @Schema(description = "coordinate tuple in geoJson format [longitude, latitude]")
    private List<Double> endCoordinates;
    private String peopleNumber;
    private String assets;
    private String passengers;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private LocalDateTime requestedDate;
    private TripTypeEnum tripType;
    private boolean isShortNotice;
    private Long correlative;

    private UUID transportationRequestStateId;
    private TransportationRequestStateResponse transportationRequestState;

    private UUID personRequestedId;
    private PersonResponse personRequested;

    private Set<ReasonResponse> rejectReasons;
    private Set<ReasonResponse> cancelReasons;
    private List<RequestLocationResponse> requestLocations;
}
