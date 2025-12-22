package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.service.ScheduleTransportationService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.ScheduleTransportationSpec;
import com.kernotec.driverscheduleservice.rest.dto.response.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.transportation.ScheduleTransportationResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = ScheduleTransportationSpec.TAG_NAME,
     description = ScheduleTransportationSpec.TAG_DESCRIPTION)
@RequestMapping(path = ScheduleTransportationSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class ScheduleTransportationController {

    private final ScheduleTransportationService scheduleTransportationService;
    private final ScheduleTransportationResponseMapper scheduleTransportationResponseMapper;

    @Operation(summary = "find all schedule transportations")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ScheduleTransportationResponse> findAll(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<ScheduleTransportation> scheduleTransportationPage = scheduleTransportationService.findAll(
            pageable);

        return PageResponse.<ScheduleTransportationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(scheduleTransportationResponseMapper.toResponse(
                scheduleTransportationPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(scheduleTransportationPage.getTotalElements())
                .pages(scheduleTransportationPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find schedule transportations by id")
    @GetMapping("{scheduleTransportationId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ScheduleTransportationResponse> findById(
        @PathVariable UUID scheduleTransportationId)
    {
        ScheduleTransportation scheduleTransportation = scheduleTransportationService.findByIdThrow(
            scheduleTransportationId);

        return SingleResponse.<ScheduleTransportationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(scheduleTransportationResponseMapper.toResponse(scheduleTransportation))
            .build();
    }
}
