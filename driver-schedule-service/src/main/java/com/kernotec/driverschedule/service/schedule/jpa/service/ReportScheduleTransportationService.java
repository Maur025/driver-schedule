package com.kernotec.driverschedule.service.schedule.jpa.service;

import com.kernotec.driverschedule.service.schedule.jpa.entity.ScheduleTransportation;
import com.kernotec.driverschedule.service.schedule.jpa.repository.ScheduleTransportationRepository;
import com.kernotec.driverschedule.service.schedule.jpa.specification.ScheduleTransportationSpecification;
import com.kernotec.driverschedule.service.schedule.rest.dto.request.ReportScheduleTransportationRequest;
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
