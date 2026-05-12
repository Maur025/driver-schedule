package com.kernotec.driverschedule.service.request.jpa.service;

import com.kernotec.driverschedule.service.request.jpa.entity.TransportationRequest;
import com.kernotec.driverschedule.service.request.jpa.repository.TransportationRequestRepository;
import com.kernotec.driverschedule.service.request.jpa.specification.TransportationRequestSpecification;
import com.kernotec.driverschedule.service.request.rest.dto.request.ReportTransportationRequestRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportTransportationRequestService {

    private final TransportationRequestRepository repository;

    public Page<TransportationRequest> findAllWithFilters(
        ReportTransportationRequestRequest filterRequest, Pageable pageable)
    {
        return repository.findAll(
            TransportationRequestSpecification.builder()
                .withZoneId(filterRequest.getZoneId())
                .withYearDate(filterRequest.getYearDate())
                .withMonthDate(filterRequest.getMonthDate())
                .withSimpleDate(filterRequest.getSimpleDate())
                .withDateRange(filterRequest.getFromDate(), filterRequest.getToDate())
                .withTransportationRequestStateId(filterRequest.getTransportationRequestStateId())
                .withPersonRequestedId(filterRequest.getPersonRequestedId())
                .withTransportationRequestState(filterRequest.getTransportationRequestState())
                .withTripType(filterRequest.getTripType()), pageable
        );
    }
}
