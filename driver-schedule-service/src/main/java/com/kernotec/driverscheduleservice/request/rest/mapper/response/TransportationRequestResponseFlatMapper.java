package com.kernotec.driverscheduleservice.request.rest.mapper.response;

import com.kernotec.driverscheduleservice.request.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.request.rest.dto.response.TransportationRequestResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TransportationRequestResponseFlatMapper {

    @Mapping(target = "personRequested", ignore = true)
    @Mapping(target = "rejectReasons", ignore = true)
    @Mapping(target = "cancelReasons", ignore = true)
    @Mapping(target = "scheduleTransportations", ignore = true)
    @Mapping(target = "requestCoords", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    TransportationRequestResponse toResponse(TransportationRequest transportationRequest);

    List<TransportationRequestResponse> toResponse(
        List<TransportationRequest> transportationRequestList);

    Set<TransportationRequestResponse> toResponse(
        Set<TransportationRequest> transportationRequestSet);
}
