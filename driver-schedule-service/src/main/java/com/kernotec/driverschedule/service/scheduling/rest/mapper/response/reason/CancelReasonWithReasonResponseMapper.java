package com.kernotec.driverschedule.service.scheduling.rest.mapper.response.reason;

import com.kernotec.driverschedule.service.scheduling.jpa.entity.CancelReason;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.schedule.CancelReasonResponse;
import com.kernotec.driverschedule.resource.rest.mapper.response.ReasonResponseFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ReasonResponseFlatMapper.class})
public interface CancelReasonWithReasonResponseMapper {

    @Mapping(target = "scheduleTransportation", ignore = true)
    CancelReasonResponse toResponse(CancelReason cancelReason);

    List<CancelReasonResponse> toResponse(List<CancelReason> cancelReasonList);

    Set<CancelReasonResponse> toResponse(Set<CancelReason> cancelReasonSet);
}
