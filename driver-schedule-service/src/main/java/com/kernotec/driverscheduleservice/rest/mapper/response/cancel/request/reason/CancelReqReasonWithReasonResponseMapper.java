package com.kernotec.driverscheduleservice.rest.mapper.response.cancel.request.reason;

import com.kernotec.driverscheduleservice.jpa.entity.CancelRequestReason;
import com.kernotec.driverscheduleservice.rest.dto.response.cancel.request.reason.CancelRequestReasonResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.reason.ReasonResponseFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {ReasonResponseFlatMapper.class})
public interface CancelReqReasonWithReasonResponseMapper {

    CancelRequestReasonResponse toResponse(CancelRequestReason cancelRequestReason);

    List<CancelRequestReasonResponse> toResponse(List<CancelRequestReason> cancelRequestReasonList);

    Set<CancelRequestReasonResponse> toResponse(Set<CancelRequestReason> cancelRequestReasonSet);
}
