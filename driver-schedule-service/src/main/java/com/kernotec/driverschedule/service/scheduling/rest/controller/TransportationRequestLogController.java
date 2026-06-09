package com.kernotec.driverschedule.service.scheduling.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TransportationRequestLog;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TransportationRequestLogService;
import com.kernotec.driverschedule.service.scheduling.rest.RequestApiSpec.TransportationRequestLogSpec;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.TransportationRequestLogResponse;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.TransportationRequestLogResponseMapper;
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

@Tag(name = TransportationRequestLogSpec.TAG_NAME,
     description = TransportationRequestLogSpec.TAG_DESCRIPTION)
@RequestMapping(path = TransportationRequestLogSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class TransportationRequestLogController {

    private final TransportationRequestLogService transportationRequestLogService;
    private final TransportationRequestLogResponseMapper transportationRequestLogResponseMapper;

    @Operation(summary = "find all transportation request logs")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TransportationRequestLogResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<TransportationRequestLog> transportationRequestLogPage = transportationRequestLogService.findAll(
            pageable);

        return PageResponse.<TransportationRequestLogResponse>builder()
            .code(HttpStatus.OK.value())
            .data(transportationRequestLogResponseMapper.toResponse(
                transportationRequestLogPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(transportationRequestLogPage.getTotalElements())
                .pages(transportationRequestLogPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find transportation request log by id")
    @GetMapping("{transportationRequestLogId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<TransportationRequestLogResponse> findById(
        @PathVariable("transportationRequestLogId") UUID transportationRequestLogId)
    {
        TransportationRequestLog transportationRequestLog = transportationRequestLogService.findByIdThrow(
            transportationRequestLogId);

        return SingleResponse.<TransportationRequestLogResponse>builder()
            .code(HttpStatus.OK.value())
            .data(transportationRequestLogResponseMapper.toResponse(transportationRequestLog))
            .build();
    }
}
