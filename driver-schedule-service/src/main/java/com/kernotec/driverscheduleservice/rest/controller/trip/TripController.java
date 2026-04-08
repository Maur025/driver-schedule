package com.kernotec.driverscheduleservice.rest.controller.trip;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.common.annotation.trip.CanCreateTrip;
import com.kernotec.driverscheduleservice.common.annotation.trip.CanEmergencyTrip;
import com.kernotec.driverscheduleservice.common.annotation.trip.CanFinishTrip;
import com.kernotec.driverscheduleservice.common.annotation.trip.CanReadTrip;
import com.kernotec.driverscheduleservice.common.annotation.trip.CanUpdateTrip;
import com.kernotec.driverscheduleservice.jpa.entity.trip.Trip;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.TripSpec;
import com.kernotec.driverscheduleservice.rest.command.trip.trip.ProcessFlowTripFinalizeRequestCmd;
import com.kernotec.driverscheduleservice.rest.command.trip.trip.ProcessTripCreateRequestCmd;
import com.kernotec.driverscheduleservice.rest.command.trip.trip.ProcessTripPatchUpdateRequestCmd;
import com.kernotec.driverscheduleservice.rest.command.trip.trip.emergency.ProcessTripEmergencyRequestCmd;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.TripCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.TripFilterRequest;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.TripFinalizeRequest;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.TripUpdatePatchRequest;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.emergency.TripEmergencyRequest;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.TripResponse;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.TripResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
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

@Tag(name = TripSpec.TAG_NAME, description = TripSpec.TAG_DESCRIPTION)
@RequestMapping(path = TripSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class TripController {

    private final TripService tripService;

    private final TripResponseMapper tripResponseMapper;

    private final ProcessTripCreateRequestCmd processTripCreateRequestCmd;
    private final ProcessTripPatchUpdateRequestCmd processTripPatchUpdateRequestCmd;
    private final ProcessFlowTripFinalizeRequestCmd processFlowTripFinalizeRequestCmd;
    private final ProcessTripEmergencyRequestCmd processTripEmergencyRequestCmd;

    @Operation(summary = "find all trips")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CanReadTrip
    public PageResponse<TripResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<Trip> tripPage = tripService.findAll(pageable);

        return PageResponse.<TripResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripResponseMapper.toResponse(tripPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(tripPage.getTotalElements())
                .pages(tripPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find trip by id")
    @GetMapping("{tripId}")
    @ResponseStatus(HttpStatus.OK)
    @CanReadTrip
    public SingleResponse<TripResponse> findById(@PathVariable("tripId") UUID tripId) {
        Trip trip = tripService.findByIdThrow(tripId);

        return SingleResponse.<TripResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripResponseMapper.toResponse(trip))
            .build();
    }

    @Operation(summary = "search trips")
    @PostMapping("search")
    @ResponseStatus(HttpStatus.OK)
    @CanReadTrip
    public PageResponse<TripResponse> search(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "false") Boolean descending,
        @RequestBody TripFilterRequest request)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<Trip> tripPage = tripService.findAllBySearch(request, pageable);

        return PageResponse.<TripResponse>builder()
            .code(HttpStatus.OK.value())
            .data(tripResponseMapper.toResponse(tripPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(tripPage.getTotalElements())
                .pages(tripPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "create trip")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CanCreateTrip
    public SingleResponse<TripResponse> create(@RequestBody TripCreateRequest request) {
        UUID tripId = tripService.findByTripAssignmentId(request.getTripAssignmentId())
            .map(Trip::getId)
            .orElseGet(() -> processTripCreateRequestCmd.withRequest(
                    ProcessTripCreateRequestCmd.Request.builder()
                        .tripCreateRequest(request)
                        .build())
                .execute());

        return SingleResponse.<TripResponse>builder()
            .code(HttpStatus.CREATED.value())
            .data(tripResponseMapper.toResponse(tripId))
            .build();
    }

    @Operation(summary = "trip update")
    @PatchMapping("{tripId}")
    @ResponseStatus(HttpStatus.OK)
    @CanUpdateTrip
    public SingleResponse<TripResponse> patchUpdate(@PathVariable("tripId") UUID tripId,
        @RequestBody TripUpdatePatchRequest request)
    {
        processTripPatchUpdateRequestCmd.withRequest(
                ProcessTripPatchUpdateRequestCmd.Request.builder()
                    .tripId(tripId)
                    .tripUpdatePatchRequest(request)
                    .build())
            .execute();

        return SingleResponse.<TripResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Update successfully")
            .build();
    }

    @Operation(summary = "trip finalized")
    @PostMapping("{tripId}/finalized")
    @ResponseStatus(HttpStatus.OK)
    @CanFinishTrip
    public SingleResponse<TripResponse> tripFinalize(@PathVariable("tripId") UUID tripId,
        @RequestBody TripFinalizeRequest request)
    {
        processFlowTripFinalizeRequestCmd.withRequest(
                ProcessFlowTripFinalizeRequestCmd.Request.builder()
                    .tripId(tripId)
                    .tripFinalizeRequest(request)
                    .build())
            .execute();

        return SingleResponse.<TripResponse>builder()
            .code(HttpStatus.OK.value())
            .message("successfully completed")
            .build();
    }

    @Operation(summary = "trip emergency")
    @PostMapping("{tripId}/emergency-reported")
    @ResponseStatus(HttpStatus.OK)
    @CanEmergencyTrip
    public SingleResponse<TripResponse> tripEmergency(@PathVariable("tripId") UUID tripId,
        @RequestBody TripEmergencyRequest request)
    {
        processTripEmergencyRequestCmd.withRequest(ProcessTripEmergencyRequestCmd.Request.builder()
                .tripId(tripId)
                .tripEmergencyRequest(request)
                .build())
            .execute();

        return SingleResponse.<TripResponse>builder()
            .code(HttpStatus.OK.value())
            .message("emergency reported successfully")
            .build();
    }
}
