package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.RequestLocation;
import com.kernotec.driverscheduleservice.jpa.repository.RequestLocationRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RequestLocationService extends BaseServiceImpl<RequestLocation, UUID> {

    private final RequestLocationRepository repository;

    @Override
    protected String resourceName() {
        return "Request Location";
    }

    @Override
    protected BaseRepository<RequestLocation, UUID> repository() {
        return repository;
    }
}
