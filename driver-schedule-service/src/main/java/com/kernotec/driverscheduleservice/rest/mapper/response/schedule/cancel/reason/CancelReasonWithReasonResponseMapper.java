package com.kernotec.driverscheduleservice.rest.mapper.response.schedule.cancel.reason;

import com.kernotec.driverscheduleservice.jpa.entity.schedule.CancelReason;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.cancel.reason.CancelReasonResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.reason.ReasonResponseFlatMapper;
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
