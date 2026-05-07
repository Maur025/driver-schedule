package com.kernotec.driverschedule.service.request.rest.mapper.response;

import com.kernotec.driverschedule.service.request.jpa.entity.TransportationRequestLog;
import com.kernotec.driverschedule.service.request.rest.dto.response.TransportationRequestLogResponse;
import com.kernotec.driverschedule.service.util.DateResponseUtil;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {TransportationRequestResponseFlatMapper.class, DateResponseUtil.class})
public interface TransportationRequestLogResponseMapper {

    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    TransportationRequestLogResponse toResponse(TransportationRequestLog transportationRequestLog);

    TransportationRequestLogResponse toResponse(UUID id);

    List<TransportationRequestLogResponse> toResponse(
        List<TransportationRequestLog> transportationRequestLogList);

    Set<TransportationRequestLogResponse> toResponse(
        Set<TransportationRequestLog> transportationRequestLogSet);
}
