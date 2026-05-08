package com.kernotec.driverschedule.service.jpa.service.trip;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverschedule.service.jpa.entity.trip.TripEmergency;
import com.kernotec.driverschedule.service.jpa.repository.trip.TripEmergencyRepository;
import com.kernotec.driverschedule.service.jpa.specification.trip.TripEmergencySpecification;
import com.kernotec.driverschedule.service.rest.dto.trip.request.trip.emergency.TripEmergencyFilterRequest;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TripEmergencyService extends BaseServiceImpl<TripEmergency, UUID> {

    private final TripEmergencyRepository repository;

    @Override
    protected String resourceName() {
        return "Trip Emergency";
    }

    @Override
    protected BaseRepository<TripEmergency, UUID> repository() {
        return repository;
    }

    public Page<TripEmergency> findAllBySearch(TripEmergencyFilterRequest filterRequest,
        Pageable pageable)
    {
        return repository.findAll(
            TripEmergencySpecification.builder()
                .withTripEmergencyStates(filterRequest.getTripEmergencyStates())
                .withPersonEmergencyReportedId(filterRequest.getPersonEmergencyReportedId())
                .withTripId(filterRequest.getTripId())
                .withKeyword(filterRequest.getKeyword())
                .withZoneId(filterRequest.getZoneId())
                .withSimpleDate(filterRequest.getSimpleDate())
                .withDateRange(filterRequest.getFromDate(), filterRequest.getToDate())
                .withMonthDate(filterRequest.getMonthDate())
                .withYearDate(filterRequest.getYearDate()), pageable
        );
    }
}
