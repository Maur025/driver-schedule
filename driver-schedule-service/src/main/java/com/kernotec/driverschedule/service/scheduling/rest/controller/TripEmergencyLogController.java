package com.kernotec.driverschedule.service.scheduling.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripEmergencyLog;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TripEmergencyLogService;
import com.kernotec.driverschedule.service.scheduling.rest.TripApiSpec.TripEmergencyLogSpec;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip.TripEmergencyLogResponse;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip.TripEmergencyLogResponseMapper;
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

@Tag(name = TripEmergencyLogSpec.TAG_NAME, description = TripEmergencyLogSpec.TAG_DESCRIPTION)
@RequestMapping(path = TripEmergencyLogSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class TripEmergencyLogController {

    private final TripEmergencyLogService tripEmergencyLogService;
    private final TripEmergencyLogResponseMapper tripEmergencyLogResponseMapper;

    @Operation(summary = "find all")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TripEmergencyLogResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdBy") String sortBy,
        @RequestParam(name = "descending", defaultValue = "false") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<TripEmergencyLog> tripEmergencyLogPage = tripEmergencyLogService.findAll(pageable);

        return PageResponse.<TripEmergencyLogResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripEmergencyLogResponseMapper.toResponse(tripEmergencyLogPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(tripEmergencyLogPage.getTotalElements())
                .pages(tripEmergencyLogPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{tripEmergencyLogId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<TripEmergencyLogResponse> findById(
        @PathVariable("tripEmergencyLogId") UUID tripEmergencyLogId)
    {
        TripEmergencyLog tripEmergencyLog = tripEmergencyLogService.findByIdThrow(
            tripEmergencyLogId);

        return SingleResponse.<TripEmergencyLogResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripEmergencyLogResponseMapper.toResponse(tripEmergencyLog))
            .build();
    }
}
