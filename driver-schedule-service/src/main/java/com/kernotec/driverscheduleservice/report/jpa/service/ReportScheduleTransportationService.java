package com.kernotec.driverscheduleservice.report.jpa.service;

import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.repository.ScheduleTransportationRepository;
import com.kernotec.driverscheduleservice.jpa.specification.schedule.transportation.ScheduleTransportationSpecification;
import com.kernotec.driverscheduleservice.report.rest.dto.request.ReportScheduleTransportationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportScheduleTransportationService {

    private final ScheduleTransportationRepository repository;

    public Page<ScheduleTransportation> findAllWithFilters(
        ReportScheduleTransportationRequest filterRequest, Pageable pageable)
    {
        return repository.findAll(ScheduleTransportationSpecification.builder(), pageable);
    }
}
