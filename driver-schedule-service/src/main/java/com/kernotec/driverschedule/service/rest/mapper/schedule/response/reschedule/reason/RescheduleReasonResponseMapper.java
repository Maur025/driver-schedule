package com.kernotec.driverschedule.service.rest.mapper.schedule.response.reschedule.reason;

import com.kernotec.driverschedule.service.jpa.entity.schedule.RescheduleReason;
import com.kernotec.driverschedule.service.rest.dto.schedule.response.reschedule.reason.RescheduleReasonResponse;
import com.kernotec.driverschedule.service.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationResponseFlatMapper;
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
