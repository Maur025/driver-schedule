package com.kernotec.driverscheduleservice.rest.mapper.response.request.cancel.request.reason;

import com.kernotec.driverscheduleservice.jpa.entity.request.CancelRequestReason;
import com.kernotec.driverscheduleservice.rest.dto.request.response.cancel.request.reason.CancelRequestReasonResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.reason.ReasonResponseFlatMapper;
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
