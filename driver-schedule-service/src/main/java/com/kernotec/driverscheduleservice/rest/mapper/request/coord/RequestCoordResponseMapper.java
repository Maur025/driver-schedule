package com.kernotec.driverscheduleservice.rest.mapper.request.coord;

import com.kernotec.driverscheduleservice.jpa.entity.RequestCoord;
import com.kernotec.driverscheduleservice.rest.dto.response.request.coord.RequestCoordResponse;
import com.kernotec.driverscheduleservice.rest.mapper.transportation.request.TransportationRequestResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {TransportationRequestResponseFlatMapper.class})
public interface RequestCoordResponseMapper {

    RequestCoordResponse toResponse(RequestCoord requestCoord);

    RequestCoordResponse toResponse(UUID id);

    List<RequestCoordResponse> toResponse(List<RequestCoord> requestCoordList);

    Set<RequestCoordResponse> toResponse(Set<RequestCoord> requestCoordSet);
}
