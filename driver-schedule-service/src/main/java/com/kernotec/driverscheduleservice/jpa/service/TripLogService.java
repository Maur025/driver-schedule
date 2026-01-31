package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.TripLog;
import com.kernotec.driverscheduleservice.jpa.repository.TripLogRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TripLogService extends BaseServiceImpl<TripLog, UUID> {

    private final TripLogRepository repository;

    @Override
    protected String resourceName() {
        return "Trip Log";
    }

    @Override
    protected BaseRepository<TripLog, UUID> repository() {
        return repository;
    }
}
