package com.kernotec.driverschedule.service.report.jpa.service;

import com.kernotec.driverschedule.service.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverschedule.service.jpa.repository.schedule.ScheduleTransportationRepository;
import com.kernotec.driverschedule.service.jpa.specification.schedule.ScheduleTransportationSpecification;
import com.kernotec.driverschedule.service.report.rest.dto.request.ReportScheduleTransportationRequest;
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
