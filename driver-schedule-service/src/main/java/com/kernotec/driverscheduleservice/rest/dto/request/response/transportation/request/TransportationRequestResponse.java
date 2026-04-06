package com.kernotec.driverscheduleservice.rest.dto.request.response.transportation.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverscheduleservice.audit.user.dto.response.AuditEntityUserResponse;
import com.kernotec.driverscheduleservice.jpa.enums.request.TripTypeEnum;
import com.kernotec.driverscheduleservice.rest.dto.request.response.cancel.request.reason.CancelRequestReasonResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.person.PersonResponse;
import com.kernotec.driverscheduleservice.rest.dto.request.response.reject.reason.RejectReasonResponse;
import com.kernotec.driverscheduleservice.rest.dto.request.response.request.coord.RequestCoordResponse;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.rest.dto.request.response.transportation.request.state.TransportationRequestStateResponse;
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
public class TransportationRequestResponse extends AuditEntityUserResponse {

    private String peopleNumber;
    private String assets;
    private String passengers;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private LocalDateTime requestedDate;
    private ZonedDateTime requestedFrom;
    private ZonedDateTime requestedTo;
    private TripTypeEnum tripType;
    private boolean isShortNotice;
    private Long correlative;
    private String detail;
    private boolean isAssetPickup;
    private Double estimatedTotalDistanceKm;
    private Double estimatedTotalDurationMin;
    private String code;
    private boolean wasRequestedByScheduler;

    private UUID transportationRequestStateId;
    private TransportationRequestStateResponse transportationRequestState;

    private UUID personRequestedId;
    private PersonResponse personRequested;

    private Set<ScheduleTransportationResponse> scheduleTransportations;

    private List<RejectReasonResponse> rejectReasons;
    private List<CancelRequestReasonResponse> cancelReasons;
    private List<RequestCoordResponse> requestCoords;
}
