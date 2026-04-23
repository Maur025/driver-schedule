package com.kernotec.driverscheduleservice.rest.dto.schedule.response.schedule.transportation.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverscheduleservice.audit.user.dto.response.AuditEntityUserResponse;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.schedule.transportation.state.ScheduleTransportationStateResponse;
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
