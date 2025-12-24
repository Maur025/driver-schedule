package com.kernotec.driverscheduleservice.util;

import com.kernotec.driverscheduleservice.jpa.util.Coordinate;
import java.util.List;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

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
