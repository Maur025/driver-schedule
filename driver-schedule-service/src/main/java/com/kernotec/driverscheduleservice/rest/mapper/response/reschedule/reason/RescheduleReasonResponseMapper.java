package com.kernotec.driverscheduleservice.rest.mapper.response.reschedule.reason;

import com.kernotec.driverscheduleservice.jpa.entity.schedule.RescheduleReason;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.reschedule.reason.RescheduleReasonResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.schedule.transportation.ScheduleTransportationResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {ScheduleTransportationResponseFlatMapper.class})
public interface RescheduleReasonResponseMapper {

    RescheduleReasonResponse toResponse(UUID id);

    RescheduleReasonResponse toResponse(RescheduleReason rescheduleReason);

    List<RescheduleReasonResponse> toResponse(List<RescheduleReason> rescheduleReasonList);

    Set<RescheduleReasonResponse> toResponse(Set<RescheduleReason> rescheduleReasonSet);
}
