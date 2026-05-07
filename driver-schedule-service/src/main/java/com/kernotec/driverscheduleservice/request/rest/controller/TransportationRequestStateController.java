package com.kernotec.driverscheduleservice.request.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.request.jpa.entity.TransportationRequestState;
import com.kernotec.driverscheduleservice.request.jpa.service.TransportationRequestStateService;
import com.kernotec.driverscheduleservice.request.rest.ApiRequestSpec.TransportationRequestStateSpec;
import com.kernotec.driverscheduleservice.request.rest.dto.response.TransportationRequestStateResponse;
import com.kernotec.driverscheduleservice.request.rest.mapper.response.TransportationRequestStateResponseMapper;
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

@Tag(name = TransportationRequestStateSpec.TAG_NAME,
     description = TransportationRequestStateSpec.TAG_DESCRIPTION)
@RequestMapping(path = TransportationRequestStateSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class TransportationRequestStateController {

    private final TransportationRequestStateService transportationRequestStateService;
    private final TransportationRequestStateResponseMapper transportationRequestStateResponseMapper;

    @Operation(summary = "find all transportation request states")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TransportationRequestStateResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<TransportationRequestState> transportationRequestStatePage = transportationRequestStateService.findAll(
            pageable);

        return PageResponse.<TransportationRequestStateResponse>builder()
            .code(HttpStatus.OK.value())
            .data(transportationRequestStateResponseMapper.toResponse(
                transportationRequestStatePage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(transportationRequestStatePage.getTotalElements())
                .pages(transportationRequestStatePage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find transportation request states without pagination")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Deprecated
    public PageResponse<TransportationRequestStateResponse> findAllWithoutPagination() {
        List<TransportationRequestState> transportationRequestStateList = transportationRequestStateService.findAll();

        return PageResponse.<TransportationRequestStateResponse>builder()
            .code(HttpStatus.OK.value())
            .data(
                transportationRequestStateResponseMapper.toResponse(transportationRequestStateList))
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{transportationRequestStateId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<TransportationRequestStateResponse> findById(
        @PathVariable("transportationRequestStateId") UUID transportationRequestStateId)
    {
        TransportationRequestState transportationRequestState = transportationRequestStateService.findByIdThrow(
            transportationRequestStateId);

        return SingleResponse.<TransportationRequestStateResponse>builder()
            .code(HttpStatus.OK.value())
            .data(transportationRequestStateResponseMapper.toResponse(transportationRequestState))
            .build();
    }
}
