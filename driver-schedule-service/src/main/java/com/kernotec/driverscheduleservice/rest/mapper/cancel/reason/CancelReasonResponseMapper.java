package com.kernotec.driverscheduleservice.rest.mapper.cancel.reason;

import com.kernotec.driverscheduleservice.jpa.entity.CancelReason;
import com.kernotec.driverscheduleservice.rest.dto.response.CancelReasonResponse;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.transportation.ScheduleTransportationResponseFlatMapper;
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
