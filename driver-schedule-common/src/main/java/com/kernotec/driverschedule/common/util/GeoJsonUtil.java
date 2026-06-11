package com.kernotec.driverschedule.common.util;

import com.kernotec.driverschedule.common.dto.Coordinate;
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
}
