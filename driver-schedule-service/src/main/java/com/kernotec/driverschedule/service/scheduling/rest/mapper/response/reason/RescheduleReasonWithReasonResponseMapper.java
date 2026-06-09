package com.kernotec.driverschedule.service.scheduling.rest.mapper.response.reason;

import com.kernotec.driverschedule.service.scheduling.jpa.entity.RescheduleReason;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.schedule.RescheduleReasonResponse;
import com.kernotec.driverschedule.resource.rest.mapper.response.ReasonResponseFlatMapper;
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
