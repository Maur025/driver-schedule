package com.kernotec.driverschedule.service.rest.controller.resource;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.service.common.annotation.location.CanCreateLocation;
import com.kernotec.driverschedule.service.common.annotation.location.CanReadLocation;
import com.kernotec.driverschedule.service.common.annotation.location.CanUpdateLocation;
import com.kernotec.driverschedule.service.jpa.entity.resource.Location;
import com.kernotec.driverschedule.service.jpa.service.resource.LocationService;
import com.kernotec.driverschedule.service.rest.ApiSpec.LocationSpec;
import com.kernotec.driverschedule.service.rest.command.resource.location.ProcessLocationCreateRequestCmd;
import com.kernotec.driverschedule.service.rest.command.resource.location.ProcessLocationUpdateRequestCmd;
import com.kernotec.driverschedule.service.rest.dto.resource.request.location.LocationCreateRequest;
import com.kernotec.driverschedule.service.rest.dto.resource.request.location.LocationUpdateRequest;
import com.kernotec.driverschedule.service.rest.dto.resource.response.location.LocationResponse;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.location.LocationResponseMapper;
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
    @CanReadLocation
    public PageResponse<LocationResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") Boolean descending,
        @RequestParam(name = "keyword", required = false) String keyword)
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
    @CanReadLocation
    @Deprecated
    public PageResponse<LocationResponse> findAllWithoutPagination()
    {
        Pageable pageable = PageableUtil.of(0, 20, "name", false);

        Page<Location> locationPage = locationService.findAll(pageable);

        return PageResponse.<LocationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(locationResponseMapper.toResponse(locationPage.getContent()))
            .build();
    }

    @Operation(summary = "find location by id")
    @GetMapping("{locationId}")
    @ResponseStatus(HttpStatus.OK)
    @CanReadLocation
    public SingleResponse<LocationResponse> findById(@PathVariable("locationId") UUID locationId) {
        Location location = locationService.findByIdThrow(locationId);

        return SingleResponse.<LocationResponse>builder()
            .code(HttpStatus.OK.value())
            .data(locationResponseMapper.toResponse(location))
            .build();
    }

    @Operation(summary = "save location")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CanCreateLocation
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
    @CanUpdateLocation
    public SingleResponse<LocationResponse> update(@PathVariable("locationId") UUID locationId,
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
