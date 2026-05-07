package com.kernotec.driverschedule.service.rest.mapper.schedule.response.reschedule.reason;

import com.kernotec.driverschedule.service.jpa.entity.schedule.RescheduleReason;
import com.kernotec.driverschedule.service.rest.dto.schedule.response.reschedule.reason.RescheduleReasonResponse;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.reason.ReasonResponseFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ReasonResponseFlatMapper.class})
public interface RescheduleReasonWithReasonResponseMapper {

    @Mapping(target = "scheduleTransportation", ignore = true)
    RescheduleReasonResponse toResponse(RescheduleReason rescheduleReason);

    List<RescheduleReasonResponse> toResponse(List<RescheduleReason> rescheduleReasonList);

    Set<RescheduleReasonResponse> toResponse(Set<RescheduleReason> rescheduleReasonSet);
}
