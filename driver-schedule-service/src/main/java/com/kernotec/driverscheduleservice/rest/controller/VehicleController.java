package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.jpa.entity.Vehicle;
import com.kernotec.driverscheduleservice.jpa.service.VehicleService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.VehicleSpec;
import com.kernotec.driverscheduleservice.rest.dto.response.VehicleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.vehicle.VehicleResponseMapper;
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

@Tag(name = VehicleSpec.TAG_NAME, description = VehicleSpec.TAG_DESCRIPTION)
@RequestMapping(path = VehicleSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleResponseMapper vehicleResponseMapper;

    @Operation(summary = "find all vehicles")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<VehicleResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "0") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<Vehicle> vehiclePage = vehicleService.findAll(pageable);

        return PageResponse.<VehicleResponse>builder()
            .code(HttpStatus.OK.value())
            .data(vehicleResponseMapper.toResponse(vehiclePage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(vehiclePage.getTotalElements())
                .pages(vehiclePage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find vehicles without pagination")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<VehicleResponse> findAllWithoutPagination() {
        List<Vehicle> vehicleList = vehicleService.findAll();

        return PageResponse.<VehicleResponse>builder()
            .code(HttpStatus.OK.value())
            .data(vehicleResponseMapper.toResponse(vehicleList))
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{vehicleId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<VehicleResponse> findById(@PathVariable() UUID vehicleId)
    {
        Vehicle vehicle = vehicleService.findByIdThrow(vehicleId);

        return SingleResponse.<VehicleResponse>builder()
            .code(HttpStatus.OK.value())
            .data(vehicleResponseMapper.toResponse(vehicle))
            .build();
    }
}
