package com.kernotec.driverschedule.service.schedule.rest.mapper.response.reason;

import com.kernotec.driverschedule.service.schedule.jpa.entity.RescheduleReason;
import com.kernotec.driverschedule.service.schedule.rest.dto.response.RescheduleReasonResponse;
import com.kernotec.driverschedule.service.schedule.rest.mapper.response.ScheduleTransportationResponseFlatMapper;
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
