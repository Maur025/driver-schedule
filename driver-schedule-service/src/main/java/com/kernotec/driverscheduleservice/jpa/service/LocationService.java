package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.exception.LocationException;
import com.kernotec.driverscheduleservice.jpa.entity.Location;
import com.kernotec.driverscheduleservice.jpa.repository.LocationRepository;
import com.kernotec.driverscheduleservice.jpa.specification.location.LocationSpecification;
import com.kernotec.driverscheduleservice.jpa.util.Coordinate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
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

        Double lng = coords.get(0);
        Double lat = coords.get(1);

        if (lng == null || lat == null) {
            log.warn("Invalid data in coordinate, return null object");
            return null;
        }

        var coordinate = new Coordinate();

        coordinate.setLat(lat);
        coordinate.setLng(lng);
        return coordinate;
    }

    public Page<Location> findAllByKeyword(String keyword, Pageable pageable) {
        return repository.findAll(
            LocationSpecification.builder()
                .withKeyword(keyword), pageable
        );
    }
}
