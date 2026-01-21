package com.kernotec.driverscheduleservice.report.jpa.service;

import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.repository.TransportationRequestRepository;
import com.kernotec.driverscheduleservice.jpa.specification.transportation.request.TransportationRequestSpecification;
import com.kernotec.driverscheduleservice.report.rest.dto.request.ReportTransportationRequestRequest;
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
