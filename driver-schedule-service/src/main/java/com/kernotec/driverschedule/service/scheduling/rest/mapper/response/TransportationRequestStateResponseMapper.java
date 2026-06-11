package com.kernotec.driverschedule.service.scheduling.rest.mapper.response;

import com.kernotec.driverschedule.service.scheduling.jpa.entity.TransportationRequestState;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.TransportationRequestStateResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface TransportationRequestStateResponseMapper {

    TransportationRequestStateResponse toResponse(UUID id);

    TransportationRequestStateResponse toResponse(
        TransportationRequestState transportationRequestState);

    List<TransportationRequestStateResponse> toResponse(
        List<TransportationRequestState> transportationRequestStateList);

    Set<TransportationRequestStateResponse> toResponse(
        Set<TransportationRequestState> transportationRequestStateSet);
}
