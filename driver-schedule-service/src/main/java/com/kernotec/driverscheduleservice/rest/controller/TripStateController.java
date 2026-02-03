package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.jpa.entity.TripState;
import com.kernotec.driverscheduleservice.jpa.service.TripStateService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.TripStateSpec;
import com.kernotec.driverscheduleservice.rest.dto.response.LookupResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.trip.state.TripStateLookupResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.trip.state.TripStateResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.trip.state.TripStateResponseMapper;
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

@Tag(name = TripStateSpec.TAG_NAME, description = TripStateSpec.TAG_DESCRIPTION)
@RequestMapping(path = TripStateSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class TripStateController {

    private final TripStateService tripStateService;
    private final TripStateResponseMapper tripStateResponseMapper;

    @Operation(summary = "find all trip states")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TripStateResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<TripState> tripStatePage = tripStateService.findAll(pageable);

        return PageResponse.<TripStateResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripStateResponseMapper.toResponse(tripStatePage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(tripStatePage.getTotalElements())
                .pages(tripStatePage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find trip state by id")
    @GetMapping("{tripStateId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<TripStateResponse> findById(@PathVariable UUID tripStateId) {
        TripState tripState = tripStateService.findByIdThrow(tripStateId);

        return SingleResponse.<TripStateResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripStateResponseMapper.toResponse(tripState))
            .build();
    }

    @Operation(summary = "find all to lookup")
    @GetMapping("lookup")
    @ResponseStatus(HttpStatus.OK)
    public LookupResponse<List<TripStateLookupResponse>> findAllToLookup(
        @RequestParam(required = false) String keyword)
    {
        Pageable pageable = PageableUtil.of(0, 500, "name", false);
        Page<TripStateLookupResponse> placeCategoryResponsePage = tripStateService.findAllToLookup(
            keyword, pageable);

        return LookupResponse.<List<TripStateLookupResponse>>builder()
            .code(HttpStatus.OK.value())
            .data(placeCategoryResponsePage.getContent())
            .build();
    }
}
