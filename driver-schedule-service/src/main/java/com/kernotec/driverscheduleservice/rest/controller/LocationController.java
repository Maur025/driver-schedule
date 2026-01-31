package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.jpa.entity.Location;
import com.kernotec.driverscheduleservice.jpa.service.LocationService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.LocationSpec;
import com.kernotec.driverscheduleservice.rest.command.location.ProcessLocationCreateRequestCmd;
import com.kernotec.driverscheduleservice.rest.command.location.ProcessLocationUpdateRequestCmd;
import com.kernotec.driverscheduleservice.rest.dto.request.location.LocationCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.request.location.LocationUpdateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.location.LocationResponse;
import com.kernotec.driverscheduleservice.rest.mapper.location.LocationResponseMapper;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = LocationSpec.TAG_NAME, description = LocationSpec.TAG_DESCRIPTION)
@RequestMapping(path = LocationSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final LocationResponseMapper locationResponseMapper;
    private final ProcessLocationCreateRequestCmd processLocationCreateRequestCmd;
    private final ProcessLocationUpdateRequestCmd processLocationUpdateRequestCmd;

    @Operation(summary = "find all locations")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<LocationResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") Boolean descending,
        @RequestParam(required = false) String keyword)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        Page<Location> locationPage = locationService.findAllByKeyword(keyword, pageable);

        return PageResponse.<LocationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(locationResponseMapper.toResponse(locationPage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(locationPage.getTotalElements())
                .pages(locationPage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find all without pagination")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<LocationResponse> findAllWithoutPagination()
    {
        List<Location> locationList = locationService.findAll();

        return PageResponse.<LocationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(locationResponseMapper.toResponse(locationList))
            .build();
    }

    @Operation(summary = "find location by id")
    @GetMapping("{locationId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<LocationResponse> findById(@PathVariable UUID locationId) {
        Location location = locationService.findByIdThrow(locationId);

        return SingleResponse.<LocationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(locationResponseMapper.toResponse(location))
            .build();
    }

    @Operation(summary = "save location")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResponse<LocationResponse> save(@RequestBody LocationCreateRequest request) {
        UUID locationId = processLocationCreateRequestCmd.withRequest(
                ProcessLocationCreateRequestCmd.Request.builder()
                    .locationCreateRequest(request)
                    .build())
            .execute();

        return SingleResponse.<LocationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(locationResponseMapper.toResponse(locationId))
            .build();
    }

    @Operation(summary = "update location")
    @PutMapping("{locationId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<LocationResponse> update(@PathVariable UUID locationId,
        @RequestBody LocationUpdateRequest request)
    {
        processLocationUpdateRequestCmd.withRequest(
                ProcessLocationUpdateRequestCmd.Request.builder()
                    .locationId(locationId)
                    .locationUpdateRequest(request)
                    .build())
            .execute();

        return SingleResponse.<LocationResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Location updated successfully")
            .build();
    }
}
