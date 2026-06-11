package com.kernotec.driverschedule.service.scheduling.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.driverschedule.report.rest.ReportApiSpec.ReportSpec;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.ScheduleTransportation;
import com.kernotec.driverschedule.service.scheduling.jpa.service.ReportScheduleTransportationService;
import com.kernotec.driverschedule.service.scheduling.rest.dto.request.schedule.ReportScheduleTransportationRequest;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.schedule.ScheduleTransportationResponse;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.schedule.ScheduleTransportationResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = ReportSpec.TAG_NAME, description = ReportSpec.TAG_DESCRIPTION)
@RequestMapping(path = ReportSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ReportScheduleTransportationController {


    private final ReportScheduleTransportationService reportScheduleTransportationService;
    private final ScheduleTransportationResponseMapper scheduleTransportationResponseMapper;

    @Operation(summary = "response transportation report preview with filters")
    @PostMapping("schedule-transportation/report")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ScheduleTransportationResponse> findAllWithFilters(
        @RequestBody ReportScheduleTransportationRequest request,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<ScheduleTransportation> scheduleTransportationPage = reportScheduleTransportationService.findAllWithFilters(
            request, pageable);

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
}
