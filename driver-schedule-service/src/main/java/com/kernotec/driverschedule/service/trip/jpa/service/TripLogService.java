package com.kernotec.driverschedule.service.trip.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.trip.jpa.entity.TripLog;
import com.kernotec.driverschedule.service.trip.jpa.repository.TripLogRepository;
import com.kernotec.driverschedule.service.trip.jpa.specification.TripLogSpecification;
import com.kernotec.driverschedule.service.trip.rest.dto.request.TripLogFilterRequest;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<TripLog> findAllBySearch(TripLogFilterRequest filterRequest, Pageable pageable) {
        return repository.findAll(
            TripLogSpecification.builder()
                .withZoneId(filterRequest.getZoneId())
                .withSimpleDate(filterRequest.getSimpleDate())
                .withDateRange(filterRequest.getFromDate(), filterRequest.getToDate())
                .withMonthDate(filterRequest.getMonthDate())
                .withYearDate(filterRequest.getYearDate())
                .withTripId(filterRequest.getTripId())
                .withTripStates(filterRequest.getTripStates()), pageable
        );
    }
}
