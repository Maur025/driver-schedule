package com.kernotec.driverschedule.service.request.rest.mapper.response;

import com.kernotec.driverschedule.service.request.jpa.entity.RejectReason;
import com.kernotec.driverschedule.service.request.rest.dto.response.RejectReasonResponse;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.reason.ReasonResponseFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ReasonResponseFlatMapper.class})
public interface RejectReasonWithReasonResponseMapper {

    @Mapping(target = "transportationRequest", ignore = true)
    RejectReasonResponse toResponse(RejectReason rejectReason);

    List<RejectReasonResponse> toResponse(List<RejectReason> rejectReasonList);

    Set<RejectReasonResponse> toResponse(Set<RejectReason> rejectReasonSet);
}
