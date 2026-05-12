package com.kernotec.driverschedule.service.schedule.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverschedule.common.audit.user.dto.response.AuditEntityUserResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ScheduleTransportationLogResponse extends AuditEntityUserResponse {

    private UUID scheduleTransportationId;
    private ScheduleTransportationResponse scheduleTransportation;

    private UUID scheduleTransportationStateId;
    private ScheduleTransportationStateResponse scheduleTransportationState;
}
