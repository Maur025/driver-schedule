package com.kernotec.driverschedule.service.resource.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class PersonScheduleConflictRequest extends BaseRequest {

    private ZonedDateTime conflictValidationFrom;
    private ZonedDateTime conflictValidationTo;
    private String zoneId;
    private UUID scheduleTransportationExcludeId;
}
