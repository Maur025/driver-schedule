package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.exception.LocationException;
import com.kernotec.driverscheduleservice.jpa.entity.Location;
import com.kernotec.driverscheduleservice.jpa.repository.LocationRepository;
import com.kernotec.driverscheduleservice.jpa.util.Coordinate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LocationService extends BaseServiceImpl<Location, UUID> {

    private final LocationRepository repository;

    @Override
    protected String resourceName() {
        return "Location";
    }

    @Override
    protected BaseRepository<Location, UUID> repository() {
        return repository;
    }

    public Coordinate getCoordinateOfList(List<Double> coords) {
        if (coords == null) {
            throw new LocationException("Coordinates list is null");
        }

        if (coords.size() < 2) {
            throw new LocationException("invalid.coordinate", "", HttpStatus.BAD_REQUEST.value());
        }

        var coordinate = new Coordinate();

        coordinate.setLat(coords.get(1));
        coordinate.setLng(coords.get(0));
        return coordinate;
    }
}
