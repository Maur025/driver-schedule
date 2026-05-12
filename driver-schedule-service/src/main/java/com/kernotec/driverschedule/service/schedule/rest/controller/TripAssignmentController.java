package com.kernotec.driverschedule.service.schedule.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.service.schedule.jpa.entity.TripAssignment;
import com.kernotec.driverschedule.service.schedule.jpa.service.TripAssignmentService;
import com.kernotec.driverschedule.service.schedule.rest.ScheduleApiSpec.TripAssignmentSpec;
import com.kernotec.driverschedule.service.schedule.rest.dto.request.TripAssignmentFilterRequest;
import com.kernotec.driverschedule.service.schedule.rest.dto.response.TripAssignmentResponse;
import com.kernotec.driverschedule.service.schedule.rest.mapper.response.assignment.TripAssignmentResponseMapper;
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

@Tag(name = TripAssignmentSpec.TAG_NAME, description = TripAssignmentSpec.TAG_DESCRIPTION)
@RequestMapping(path = TripAssignmentSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class TripAssignmentController {

    private final TripAssignmentService tripAssignmentService;
    private final TripAssignmentResponseMapper tripAssignmentResponseMapper;

    @Operation(summary = "find all trip assignments")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TripAssignmentResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<TripAssignment> tripAssignmentPage = tripAssignmentService.findAll(pageable);

        return PageResponse.<TripAssignmentResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripAssignmentResponseMapper.toResponse(tripAssignmentPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(tripAssignmentPage.getTotalElements())
                .pages(tripAssignmentPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find trip assignment by id")
    @GetMapping("{tripAssignmentId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<TripAssignmentResponse> findById(
        @PathVariable("tripAssignmentId") UUID tripAssignmentId)
    {
        TripAssignment tripAssignment = tripAssignmentService.findByIdThrow(tripAssignmentId);

        return SingleResponse.<TripAssignmentResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripAssignmentResponseMapper.toResponse(tripAssignment))
            .build();
    }

    @Operation(summary = "search trip assignment")
    @PostMapping("search")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TripAssignmentResponse> search(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "false") Boolean descending,
        @RequestBody TripAssignmentFilterRequest request)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<TripAssignment> tripAssignmentPage = tripAssignmentService.findAllBySearch(
            request, pageable);

        return PageResponse.<TripAssignmentResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripAssignmentResponseMapper.toResponse(tripAssignmentPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(tripAssignmentPage.getTotalElements())
                .pages(tripAssignmentPage.getTotalPages())
                .build())
            .build();
    }
}
