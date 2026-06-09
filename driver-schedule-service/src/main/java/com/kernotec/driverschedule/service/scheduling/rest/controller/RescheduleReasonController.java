package com.kernotec.driverschedule.service.scheduling.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.RescheduleReason;
import com.kernotec.driverschedule.service.scheduling.jpa.service.RescheduleReasonService;
import com.kernotec.driverschedule.service.scheduling.rest.ScheduleApiSpec.RescheduleReasonSpec;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.schedule.RescheduleReasonResponse;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.reason.RescheduleReasonResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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

@Tag(name = RescheduleReasonSpec.TAG_NAME, description = RescheduleReasonSpec.TAG_DESCRIPTION)
@RequestMapping(path = RescheduleReasonSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class RescheduleReasonController {

    private final RescheduleReasonService rescheduleReasonService;
    private final RescheduleReasonResponseMapper rescheduleReasonResponseMapper;

    @Operation(summary = "find all reschedule reasons")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<RescheduleReasonResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<RescheduleReason> rescheduleReasonPage = rescheduleReasonService.findAll(pageable);

        return PageResponse.<RescheduleReasonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(rescheduleReasonResponseMapper.toResponse(rescheduleReasonPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(rescheduleReasonPage.getTotalElements())
                .pages(rescheduleReasonPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find reschedule reasons without pagination")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Deprecated
    public PageResponse<RescheduleReasonResponse> findAllWithoutPagination() {
        List<RescheduleReason> rescheduleReasonList = rescheduleReasonService.findAll();

        return PageResponse.<RescheduleReasonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(rescheduleReasonResponseMapper.toResponse(rescheduleReasonList))
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{rescheduleReasonId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<RescheduleReasonResponse> findById(
        @PathVariable("rescheduleReasonId") UUID rescheduleReasonId)
    {
        RescheduleReason rescheduleReason = rescheduleReasonService.findByIdThrow(
            rescheduleReasonId);

        return SingleResponse.<RescheduleReasonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(rescheduleReasonResponseMapper.toResponse(rescheduleReason))
            .build();
    }
}
