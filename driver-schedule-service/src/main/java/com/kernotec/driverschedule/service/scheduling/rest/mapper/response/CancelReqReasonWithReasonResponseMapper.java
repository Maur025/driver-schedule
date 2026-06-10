package com.kernotec.driverschedule.service.scheduling.rest.mapper.response;

import com.kernotec.driverschedule.service.scheduling.jpa.entity.CancelRequestReason;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.CancelRequestReasonResponse;
import com.kernotec.driverschedule.resource.rest.mapper.response.ReasonResponseFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ReasonResponseFlatMapper.class})
public interface CancelReqReasonWithReasonResponseMapper {

    @Mapping(target = "transportationRequest", ignore = true)
    CancelRequestReasonResponse toResponse(CancelRequestReason cancelRequestReason);

    List<CancelRequestReasonResponse> toResponse(List<CancelRequestReason> cancelRequestReasonList);

    Set<CancelRequestReasonResponse> toResponse(Set<CancelRequestReason> cancelRequestReasonSet);
}
