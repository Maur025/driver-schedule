package com.kernotec.driverscheduleservice.rest.mapper.request.response.transportation.request.state;

import com.kernotec.driverscheduleservice.jpa.entity.request.TransportationRequestState;
import com.kernotec.driverscheduleservice.rest.dto.request.response.transportation.request.state.TransportationRequestStateResponse;
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
