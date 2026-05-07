package com.kernotec.driverscheduleservice.request.rest.mapper.response;

import com.kernotec.driverscheduleservice.request.jpa.entity.RejectReason;
import com.kernotec.driverscheduleservice.request.rest.dto.response.RejectReasonResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = TransportationRequestResponseFlatMapper.class)
public interface RejectReasonResponseMapper {

    RejectReasonResponse toResponse(RejectReason rejectReason);

    RejectReasonResponse toResponse(UUID id);

    List<RejectReasonResponse> toResponse(List<RejectReason> rejectReasonList);

    Set<RejectReasonResponse> toResponse(Set<RejectReason> rejectReasonSet);
}
