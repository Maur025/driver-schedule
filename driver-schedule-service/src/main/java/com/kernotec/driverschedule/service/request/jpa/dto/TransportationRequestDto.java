package com.kernotec.driverschedule.service.request.jpa.dto;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.driverschedule.service.jpa.dto.resource.PersonDto;
import com.kernotec.driverschedule.service.jpa.dto.resource.ReasonDto;
import com.kernotec.driverschedule.service.request.jpa.enums.TripTypeEnum;
import com.kernotec.driverschedule.service.common.dto.Coordinate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportationRequestDto extends AuditEntityDto {

    private Coordinate startingCoordinate;
    private Coordinate endCoordinate;

    private String peopleNumber;
    private String assets;
    private String passengers;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private LocalDateTime requestedDate;
    private TripTypeEnum tripType;
    private boolean isShortNotice;

    private UUID transportationRequestStateId;
    private TransportationRequestStateDto transportationRequestState;

    private UUID personRequestedId;
    private PersonDto personRequested;

    private Set<ReasonDto> rejectReasons;
}
