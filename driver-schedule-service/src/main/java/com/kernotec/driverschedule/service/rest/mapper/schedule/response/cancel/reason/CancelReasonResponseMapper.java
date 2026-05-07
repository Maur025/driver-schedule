package com.kernotec.driverschedule.service.rest.mapper.schedule.response.cancel.reason;

import com.kernotec.driverschedule.service.jpa.entity.schedule.CancelReason;
import com.kernotec.driverschedule.service.rest.dto.schedule.response.cancel.reason.CancelReasonResponse;
import com.kernotec.driverschedule.service.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationResponseFlatMapper;
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
