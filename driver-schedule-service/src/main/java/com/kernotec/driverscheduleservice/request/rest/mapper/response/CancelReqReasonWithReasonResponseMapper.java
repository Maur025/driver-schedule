package com.kernotec.driverscheduleservice.request.rest.mapper.response;

import com.kernotec.driverscheduleservice.request.jpa.entity.CancelRequestReason;
import com.kernotec.driverscheduleservice.request.rest.dto.response.CancelRequestReasonResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.reason.ReasonResponseFlatMapper;
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
