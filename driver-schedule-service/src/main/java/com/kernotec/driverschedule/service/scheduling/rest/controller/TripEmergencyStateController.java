package com.kernotec.driverschedule.service.scheduling.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripEmergencyState;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TripEmergencyStateService;
import com.kernotec.driverschedule.service.scheduling.rest.TripApiSpec.TripEmergencyStateSpec;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip.TripEmergencyStateResponse;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip.TripEmergencyStateResponseMapper;
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

@Tag(name = TripEmergencyStateSpec.TAG_NAME, description = TripEmergencyStateSpec.TAG_DESCRIPTION)
@RequestMapping(path = TripEmergencyStateSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class TripEmergencyStateController {

    private final TripEmergencyStateService tripEmergencyStateService;
    private final TripEmergencyStateResponseMapper tripEmergencyStateResponseMapper;

    @Operation(summary = "find all")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TripEmergencyStateResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdBy") String sortBy,
        @RequestParam(name = "descending", defaultValue = "false") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<TripEmergencyState> tripEmergencyStatePage = tripEmergencyStateService.findAll(
            pageable);

        return PageResponse.<TripEmergencyStateResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripEmergencyStateResponseMapper.toResponse(tripEmergencyStatePage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(tripEmergencyStatePage.getTotalElements())
                .pages(tripEmergencyStatePage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{tripEmergencyStateId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<TripEmergencyStateResponse> findById(
        @PathVariable("tripEmergencyStateId") UUID tripEmergencyStateId)
    {
        TripEmergencyState tripEmergencyState = tripEmergencyStateService.findByIdThrow(
            tripEmergencyStateId);

        return SingleResponse.<TripEmergencyStateResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripEmergencyStateResponseMapper.toResponse(tripEmergencyState))
            .build();
    }
}
