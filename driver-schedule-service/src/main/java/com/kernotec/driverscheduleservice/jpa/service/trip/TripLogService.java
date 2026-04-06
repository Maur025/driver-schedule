package com.kernotec.driverscheduleservice.jpa.service.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleservice.jpa.entity.trip.TripLog;
import com.kernotec.driverscheduleservice.jpa.repository.trip.TripLogRepository;
import com.kernotec.driverscheduleservice.jpa.specification.trip.TripLogSpecification;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.log.TripLogFilterRequest;
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
