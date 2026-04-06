package com.kernotec.driverscheduleservice.rest.controller.schedule;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportationLog;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationLogService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.ScheduleTransportationLogSpec;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.schedule.transportation.log.ScheduleTransportationLogResponse;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation.log.ScheduleTransportationLogResponseMapper;
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

@Tag(name = ScheduleTransportationLogSpec.TAG_NAME,
     description = ScheduleTransportationLogSpec.TAG_DESCRIPTION)
@RequestMapping(path = ScheduleTransportationLogSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ScheduleTransportationLogController {

    private final ScheduleTransportationLogService scheduleTransportationLogService;
    private final ScheduleTransportationLogResponseMapper scheduleTransportationLogResponseMapper;

    @Operation(summary = "find all schedule transportation logs")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ScheduleTransportationLogResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<ScheduleTransportationLog> scheduleTransportationLogPage = scheduleTransportationLogService.findAll(
            pageable);

        return PageResponse.<ScheduleTransportationLogResponse>builder()
            .code(HttpStatus.OK.value())
            .data(scheduleTransportationLogResponseMapper.toResponse(
                scheduleTransportationLogPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(scheduleTransportationLogPage.getTotalElements())
                .pages(scheduleTransportationLogPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find schedule transportation log by id")
    @GetMapping("{transportationRequestLogId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ScheduleTransportationLogResponse> findById(
        @PathVariable("transportationRequestLogId") UUID transportationRequestLogId)
    {
        ScheduleTransportationLog scheduleTransportationLog = scheduleTransportationLogService.findByIdThrow(
            transportationRequestLogId);

        return SingleResponse.<ScheduleTransportationLogResponse>builder()
            .code(HttpStatus.OK.value())
            .data(scheduleTransportationLogResponseMapper.toResponse(scheduleTransportationLog))
            .build();
    }
}
