package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.command.vehicle.type.VehicleTypeCreateCmd;
import com.kernotec.driverscheduleservice.command.vehicle.type.VehicleTypeUpdateCmd;
import com.kernotec.driverscheduleservice.jpa.entity.VehicleType;
import com.kernotec.driverscheduleservice.jpa.service.VehicleTypeService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.VehicleTypeSpec;
import com.kernotec.driverscheduleservice.rest.dto.request.vehicle.type.VehicleTypeCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.request.vehicle.type.VehicleTypeUpdateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.vehicle.type.VehicleTypeResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.vehicle.type.VehicleTypeResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
@Tag(name = VehicleTypeSpec.TAG_NAME, description = VehicleTypeSpec.TAG_DESCRIPTION)
@RequestMapping(path = VehicleTypeSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class VehicleTypeController {

    private final VehicleTypeService vehicleTypeService;
    private final VehicleTypeResponseMapper vehicleTypeResponseMapper;
    private final VehicleTypeCreateCmd vehicleTypeCreateCmd;
    private final VehicleTypeUpdateCmd vehicleTypeUpdateCmd;

    @Operation(summary = "find all vehicle types")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<VehicleTypeResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<VehicleType> vehicleTypePage = vehicleTypeService.findAll(pageable);

        return PageResponse.<VehicleTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(vehicleTypeResponseMapper.toResponse(vehicleTypePage.getContent()))
            .pagination(PaginationResponse.builder()
                .count(vehicleTypePage.getTotalElements())
                .pages(vehicleTypePage.getTotalPages())
                .build())
            .build();
    }

    @Operation(summary = "find vehicle types without pagination")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<VehicleTypeResponse> findAllWithoutPagination() {
        List<VehicleType> vehicleTypeList = vehicleTypeService.findAll();

        return PageResponse.<VehicleTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(vehicleTypeResponseMapper.toResponse(vehicleTypeList))
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{vehicleTypeId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<VehicleTypeResponse> findById(@PathVariable() UUID vehicleTypeId)
    {
        VehicleType vehicleType = vehicleTypeService.findByIdThrow(vehicleTypeId);

        return SingleResponse.<VehicleTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(vehicleTypeResponseMapper.toResponse(vehicleType))
            .build();
    }

    @Operation(summary = "save vehicle type")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResponse<VehicleTypeResponse> save(@RequestBody VehicleTypeCreateRequest request) {
        UUID vehicleId = vehicleTypeCreateCmd.withRequest(VehicleTypeCreateCmd.Request.builder()
                .name(request.getName())
                .code(request.getCode())
                .build())
            .execute();

        return SingleResponse.<VehicleTypeResponse>builder()
            .code(HttpStatus.CREATED.value())
            .data(vehicleTypeResponseMapper.toResponse(vehicleId))
            .build();
    }

    @Operation(summary = "update vehicle type")
    @PatchMapping("{vehicleTypeId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<VehicleTypeResponse> update(@PathVariable UUID vehicleTypeId,
        @RequestBody VehicleTypeUpdateRequest request)
    {
        vehicleTypeUpdateCmd.withRequest(VehicleTypeUpdateCmd.Request.builder()
                .vehicleTypeId(vehicleTypeId)
                .name(request.getName())
                .build())
            .execute();

        return SingleResponse.<VehicleTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Vehicle type updated successfully")
            .build();
    }
}
