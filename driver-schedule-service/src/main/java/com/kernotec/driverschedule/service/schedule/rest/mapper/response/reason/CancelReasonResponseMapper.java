package com.kernotec.driverschedule.service.schedule.rest.mapper.response.reason;

import com.kernotec.driverschedule.service.schedule.jpa.entity.CancelReason;
import com.kernotec.driverschedule.service.schedule.rest.dto.response.CancelReasonResponse;
import com.kernotec.driverschedule.service.schedule.rest.mapper.response.ScheduleTransportationResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {ScheduleTransportationResponseFlatMapper.class})
public interface CancelReasonResponseMapper {

    CancelReasonResponse toResponse(UUID id);

    CancelReasonResponse toResponse(CancelReason cancelReason);

    List<CancelReasonResponse> toResponse(List<CancelReason> cancelReasonList);

    Set<CancelReasonResponse> toResponse(Set<CancelReason> cancelReasonSet);
}
