package com.kernotec.driverschedule.service.scheduling.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.RequestCoord;
import com.kernotec.driverschedule.service.scheduling.jpa.repository.RequestCoordRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RequestCoordService extends BaseServiceImpl<RequestCoord, UUID> {

    private final RequestCoordRepository repository;

    @Override
    protected String resourceName() {
        return "Request Coord";
    }

    @Override
    protected BaseRepository<RequestCoord, UUID> repository() {
        return repository;
    }
}
