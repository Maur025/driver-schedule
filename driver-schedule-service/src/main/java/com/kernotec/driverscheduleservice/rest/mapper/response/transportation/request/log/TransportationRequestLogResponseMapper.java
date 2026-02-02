package com.kernotec.driverscheduleservice.rest.mapper.response.transportation.request.log;

import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequestLog;
import com.kernotec.driverscheduleservice.rest.dto.response.transportation.request.log.TransportationRequestLogResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface TransportationRequestLogResponseMapper {

    TransportationRequestLogResponse toResponse(TransportationRequestLog transportationRequestLog);

    TransportationRequestLogResponse toResponse(UUID id);

    List<TransportationRequestLogResponse> toResponse(
        List<TransportationRequestLog> transportationRequestLogList);

    Set<TransportationRequestLogResponse> toResponse(
        Set<TransportationRequestLog> transportationRequestLogSet);
}
