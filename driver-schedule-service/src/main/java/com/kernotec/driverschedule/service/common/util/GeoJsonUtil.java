package com.kernotec.driverschedule.service.common.util;

import com.kernotec.driverschedule.service.common.dto.Coordinate;
import com.kernotec.driverschedule.service.request.rest.dto.request.RequestCoordCreateRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GeoJsonUtil {

    @Named("mapToPositionGeoJson")
    public List<Double> mapToPositionGeoJson(Coordinate coordinate) {
        if (coordinate == null) {
            return null;
        }

        return List.of(coordinate.getLng(), coordinate.getLat());
    }

    @Named("mapFromSplitToCoordinateObject")
    public Coordinate mapFromSplitToCoordinateObject(RequestCoordCreateRequest request) {
        if (request == null) {
            return null;
        }

        var coordinate = new Coordinate();

        coordinate.setLng(request.getLongitude());
        coordinate.setLat(request.getLatitude());

        return coordinate;
    }
}
