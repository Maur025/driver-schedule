package com.kernotec.driverscheduleservice.rest.mapper.request.response.transportation.request.log;

import com.kernotec.driverscheduleservice.jpa.entity.request.TransportationRequestLog;
import com.kernotec.driverscheduleservice.rest.dto.request.response.transportation.request.log.TransportationRequestLogResponse;
import com.kernotec.driverscheduleservice.rest.mapper.request.response.transportation.request.TransportationRequestResponseFlatMapper;
import com.kernotec.driverscheduleservice.util.DateResponseUtil;
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
