package com.kernotec.driverschedule.service.scheduling.rest.mapper.request;

import com.kernotec.driverschedule.common.dto.Coordinate;
import com.kernotec.driverschedule.common.util.GeoJsonUtil;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.RequestCoord;
import com.kernotec.driverschedule.service.scheduling.rest.dto.request.RequestCoordCreateRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

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

    @Named("mapFromSplitToCoordinateObject")
    default public Coordinate mapFromSplitToCoordinateObject(RequestCoordCreateRequest request) {
        if (request == null) {
            return null;
        }

        var coordinate = new Coordinate();

        coordinate.setLng(request.getLongitude());
        coordinate.setLat(request.getLatitude());

        return coordinate;
    }
}
