package com.kernotec.driverscheduleservice.rest.controller.trip;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.jpa.entity.trip.TripEmergency;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripEmergencyService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.TripEmergencySpec;
import com.kernotec.driverscheduleservice.rest.command.trip.trip.emergency.ProcessTripEmergencyPatchRequestCmd;
import com.kernotec.driverscheduleservice.rest.command.trip.trip.emergency.TripEmergencyDismissCmd;
import com.kernotec.driverscheduleservice.rest.command.trip.trip.emergency.TripEmergencyHandledCmd;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.emergency.TripEmergencyDismissRequest;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.emergency.TripEmergencyFilterRequest;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.emergency.TripEmergencyHandledRequest;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.emergency.TripEmergencyPatchRequest;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.emergency.TripEmergencyResponse;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.emergency.TripEmergencyResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = TripEmergencySpec.TAG_NAME, description = TripEmergencySpec.TAG_DESCRIPTION)
@RequestMapping(path = TripEmergencySpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class TripEmergencyController {

    private final TripEmergencyService tripEmergencyService;
    private final TripEmergencyResponseMapper tripEmergencyResponseMapper;
    private final ProcessTripEmergencyPatchRequestCmd processTripEmergencyPatchRequestCmd;
    private final TripEmergencyDismissCmd tripEmergencyDismissCmd;
    private final TripEmergencyHandledCmd tripEmergencyHandledCmd;

    @Operation(summary = "find all")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TripEmergencyResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "false") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<TripEmergency> tripEmergencyPage = tripEmergencyService.findAll(pageable);

        return PageResponse.<TripEmergencyResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripEmergencyResponseMapper.toResponse(tripEmergencyPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(tripEmergencyPage.getTotalElements())
                .pages(tripEmergencyPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "search trip emergencies")
    @PostMapping("search")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TripEmergencyResponse> search(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "false") Boolean descending,
        @RequestBody TripEmergencyFilterRequest request)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        Page<TripEmergency> tripEmergencyPage = tripEmergencyService.findAllBySearch(
            request, pageable);

        return PageResponse.<TripEmergencyResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripEmergencyResponseMapper.toResponse(tripEmergencyPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(tripEmergencyPage.getTotalElements())
                .pages(tripEmergencyPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{tripEmergencyId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<TripEmergencyResponse> findById(
        @PathVariable("tripEmergencyId") UUID tripEmergencyId)
    {
        TripEmergency tripEmergency = tripEmergencyService.findByIdThrow(tripEmergencyId);

        return SingleResponse.<TripEmergencyResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripEmergencyResponseMapper.toResponse(tripEmergency))
            .build();
    }

    @Operation(summary = "update")
    @PatchMapping("{tripEmergencyId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<TripEmergencyResponse> updatePatch(
        @PathVariable("tripEmergencyId") UUID tripEmergencyId,
        @RequestBody TripEmergencyPatchRequest request)
    {
        processTripEmergencyPatchRequestCmd.withRequest(
                ProcessTripEmergencyPatchRequestCmd.Request.builder()
                    .tripEmergencyId(tripEmergencyId)
                    .tripEmergencyPatchRequest(request)
                    .build())
            .execute();

        return SingleResponse.<TripEmergencyResponse>builder()
            .code(HttpStatus.OK.value())
            .message("TripEmergency updated successfully")
            .build();
    }

    @Operation(summary = "trip emergency dismissed")
    @PostMapping("{tripEmergencyId}/dismissed")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<TripEmergencyResponse> tripEmergencyDismissed(
        @PathVariable("tripEmergencyId") UUID tripEmergencyId,
        @RequestBody TripEmergencyDismissRequest request)
    {

        log.info("request reason Id: {}", request.getReasonId());

        tripEmergencyDismissCmd.withRequest(TripEmergencyDismissCmd.Request.builder()
                .tripEmergencyId(tripEmergencyId)
                .tripEmergencyDismissRequest(request)
                .build())
            .execute();

        return SingleResponse.<TripEmergencyResponse>builder()
            .code(HttpStatus.OK.value())
            .message("TripEmergency dismissed successfully")
            .build();
    }

    @Operation(summary = "trip emergency handled")
    @PostMapping("{tripEmergencyId}/handled")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<TripEmergencyResponse> tripEmergencyHandled(
        @PathVariable("tripEmergencyId") UUID tripEmergencyId,
        @RequestBody TripEmergencyHandledRequest request)
    {
        tripEmergencyHandledCmd.withRequest(TripEmergencyHandledCmd.Request.builder()
                .tripEmergencyId(tripEmergencyId)
                .tripEmergencyHandledRequest(request)
                .build())
            .execute();

        return SingleResponse.<TripEmergencyResponse>builder()
            .code(HttpStatus.OK.value())
            .message("TripEmergency handled successfully")
            .build();
    }
}
