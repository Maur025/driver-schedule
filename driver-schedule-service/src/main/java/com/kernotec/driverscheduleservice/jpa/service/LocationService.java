package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.Location;
import com.kernotec.driverscheduleservice.jpa.repository.LocationRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
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
}
