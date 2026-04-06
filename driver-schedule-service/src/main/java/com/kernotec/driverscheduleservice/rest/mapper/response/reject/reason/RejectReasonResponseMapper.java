package com.kernotec.driverscheduleservice.rest.mapper.response.reject.reason;

import com.kernotec.driverscheduleservice.jpa.entity.request.RejectReason;
import com.kernotec.driverscheduleservice.rest.dto.request.response.reject.reason.RejectReasonResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface RejectReasonResponseMapper {

    RejectReasonResponse toResponse(RejectReason rejectReason);

    RejectReasonResponse toResponse(UUID id);

    List<RejectReasonResponse> toResponse(List<RejectReason> rejectReasonList);

    Set<RejectReasonResponse> toResponse(Set<RejectReason> rejectReasonSet);
}
