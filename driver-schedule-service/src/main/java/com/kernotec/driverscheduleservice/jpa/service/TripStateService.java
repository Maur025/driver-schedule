package com.kernotec.driverscheduleservice.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.TripState;
import com.kernotec.driverscheduleservice.jpa.repository.TripStateRepository;
import com.kernotec.driverscheduleservice.rest.dto.response.trip.state.TripStateLookupResponse;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TripStateService extends BaseServiceImpl<TripState, UUID> {

    private final TripStateRepository repository;

    @Override
    protected String resourceName() {
        return "Trip State";
    }

    @Override
    protected BaseRepository<TripState, UUID> repository() {
        return repository;
    }

    public Page<TripStateLookupResponse> findAllToLookup(String keyword, Pageable pageable) {
        return repository.findAllToLookup(keyword, pageable);
    }
}
