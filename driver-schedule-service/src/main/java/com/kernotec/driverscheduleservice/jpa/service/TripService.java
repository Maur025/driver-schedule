package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.Trip;
import com.kernotec.driverscheduleservice.jpa.repository.TripRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TripService extends BaseServiceImpl<Trip, UUID> {

    private final TripRepository repository;

    @Override
    protected String resourceName() {
        return "Trip";
    }

    @Override
    protected BaseRepository<Trip, UUID> repository() {
        return repository;
    }
}
