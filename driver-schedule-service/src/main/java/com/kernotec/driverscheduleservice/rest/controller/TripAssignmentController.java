package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.jpa.entity.TripAssignment;
import com.kernotec.driverscheduleservice.jpa.service.TripAssignmentService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.TripAssignmentSpec;
import com.kernotec.driverscheduleservice.rest.dto.request.trip.assignment.TripAssignmentFilterRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.trip.assignment.TripAssignmentResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.trip.assignment.TripAssignmentResponseMapper;
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
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") Boolean descending)
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
    public SingleResponse<TripAssignmentResponse> findById(@PathVariable UUID tripAssignmentId) {
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
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "false") Boolean descending,
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
