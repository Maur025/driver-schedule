package com.kernotec.driverschedule.service.trip.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.service.trip.jpa.entity.TripObservation;
import com.kernotec.driverschedule.service.trip.jpa.service.TripObservationService;
import com.kernotec.driverschedule.service.trip.rest.TripApiSpec.TripObservationSpec;
import com.kernotec.driverschedule.service.trip.rest.dto.response.TripObservationResponse;
import com.kernotec.driverschedule.service.trip.rest.mapper.response.TripObservationResponseMapper;
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

@Tag(name = TripObservationSpec.TAG_NAME, description = TripObservationSpec.TAG_DESCRIPTION)
@RequestMapping(path = TripObservationSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class TripObservationController {

    private final TripObservationService tripObservationService;
    private final TripObservationResponseMapper tripObservationResponseMapper;

    @Operation(summary = "find all trip observations")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TripObservationResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "false") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        Page<TripObservation> tripObservationPage = tripObservationService.findAll(pageable);

        return PageResponse.<TripObservationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripObservationResponseMapper.toResponse(tripObservationPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(tripObservationPage.getTotalElements())
                .pages(tripObservationPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{tripObservationId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<TripObservationResponse> findById(
        @PathVariable("tripObservationId") UUID tripObservationId)
    {
        TripObservation tripObservation = tripObservationService.findByIdThrow(tripObservationId);

        return SingleResponse.<TripObservationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripObservationResponseMapper.toResponse(tripObservation))
            .build();
    }
}
