package com.kernotec.driverschedule.service.rest.controller.trip;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.service.jpa.entity.trip.TripLog;
import com.kernotec.driverschedule.service.jpa.service.trip.TripLogService;
import com.kernotec.driverschedule.service.rest.ApiSpec.TripLogSpec;
import com.kernotec.driverschedule.service.rest.dto.trip.request.trip.log.TripLogFilterRequest;
import com.kernotec.driverschedule.service.rest.dto.trip.response.trip.log.TripLogResponse;
import com.kernotec.driverschedule.service.rest.mapper.trip.response.trip.log.TripLogResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = TripLogSpec.TAG_NAME, description = TripLogSpec.TAG_DESCRIPTION)
@RequestMapping(path = TripLogSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class TripLogController {

    private final TripLogService tripLogService;
    private final TripLogResponseMapper tripLogResponseMapper;

    @Operation(summary = "find all trip logs")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TripLogResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<TripLog> tripLogPage = tripLogService.findAll(pageable);

        return PageResponse.<TripLogResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripLogResponseMapper.toResponse(tripLogPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(tripLogPage.getTotalElements())
                .pages(tripLogPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find trip log by id")
    @GetMapping("{tripLogId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<TripLogResponse> findById(@PathVariable("tripLogId") UUID tripLogId) {
        TripLog tripLog = tripLogService.findByIdThrow(tripLogId);

        return SingleResponse.<TripLogResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripLogResponseMapper.toResponse(tripLog))
            .build();
    }

    @Operation(summary = "find all by search")
    @PostMapping("search")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TripLogResponse> search(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "false") Boolean descending,
        @RequestBody TripLogFilterRequest request)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<TripLog> tripLogPage = tripLogService.findAllBySearch(request, pageable);

        return PageResponse.<TripLogResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripLogResponseMapper.toResponse(tripLogPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(tripLogPage.getTotalElements())
                .pages(tripLogPage.getTotalPages())
                .build())
            .build();
    }
}
