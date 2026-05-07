package com.kernotec.driverscheduleservice.request.rest.mapper.request;

import com.kernotec.driverscheduleservice.request.jpa.entity.RequestCoord;
import com.kernotec.driverscheduleservice.request.rest.dto.request.RequestCoordCreateRequest;
import com.kernotec.driverscheduleservice.util.GeoJsonUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {GeoJsonUtil.class})
public interface RequestCoordEntityMapper {

    @Mapping(target = "coordinate", source = "request",
             qualifiedByName = "mapFromSplitToCoordinateObject")
    RequestCoord toEntity(RequestCoordCreateRequest request, UUID transportationRequestId);

    default List<RequestCoord> toEntity(List<RequestCoordCreateRequest> requestList,
        UUID transportationRequestId)
    {
        if (requestList == null) {
            return null;
        }

        List<RequestCoord> list = new ArrayList<>(requestList.size());

        for (RequestCoordCreateRequest requestCoordCreateRequest : requestList) {
            list.add(toEntity(requestCoordCreateRequest, transportationRequestId));
        }

        return list;
    }
}
