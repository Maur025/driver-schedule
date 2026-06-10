package com.kernotec.driverschedule.service.scheduling.jpa.dto;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.driverschedule.person.jpa.dto.PersonDto;
import com.kernotec.driverschedule.common.dto.Coordinate;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.TripTypeEnum;
import com.kernotec.driverschedule.resource.jpa.dto.ReasonDto;
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
    private Long correlative;

    private UUID transportationRequestStateId;
    private TransportationRequestStateDto transportationRequestState;

    private UUID personRequestedId;
    private PersonDto personRequested;

    private Set<ReasonDto> rejectReasons;
}
