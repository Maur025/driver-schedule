package com.kernotec.driverscheduleservice.rest.mapper.response.reschedule.reason;

import com.kernotec.driverscheduleservice.jpa.entity.schedule.RescheduleReason;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.reschedule.reason.RescheduleReasonResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.reason.ReasonResponseFlatMapper;
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
