package com.kernotec.driverscheduleservice.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.MessageResponse;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverscheduleservice.jpa.entity.Vehicle;
import com.kernotec.driverscheduleservice.jpa.service.VehicleService;
import com.kernotec.driverscheduleservice.rest.ApiSpec.VehicleSpec;
import com.kernotec.driverscheduleservice.rest.command.vehicle.ProcessVehicleCreateRequestCmd;
import com.kernotec.driverscheduleservice.rest.command.vehicle.ProcessVehicleUpdateRequestCmd;
import com.kernotec.driverscheduleservice.rest.dto.request.vehicle.VehicleCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.request.vehicle.VehicleUpdateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.VehicleResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.vehicle.VehicleResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = VehicleSpec.TAG_NAME, description = VehicleSpec.TAG_DESCRIPTION)
@RequestMapping(path = VehicleSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleResponseMapper vehicleResponseMapper;
    private final WebSocketHandler webSocketHandler;
    private final ProcessVehicleCreateRequestCmd processVehicleCreateRequestCmd;
    private final ProcessVehicleUpdateRequestCmd processVehicleUpdateRequestCmd;

    @Operation(summary = "find all vehicles")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<VehicleResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
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

    @Operation(summary = "save vehicle")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResponse<VehicleResponse> save(@RequestBody VehicleCreateRequest request) {
        UUID vehicleId = processVehicleCreateRequestCmd.withRequest(
                ProcessVehicleCreateRequestCmd.Request.builder()
                    .vehicleCreateRequest(request)
                    .build())
            .execute();

        return SingleResponse.<VehicleResponse>builder()
            .code(HttpStatus.CREATED.value())
            .data(vehicleResponseMapper.toResponse(vehicleId))
            .build();
    }

    @Operation(summary = "update vehicle")
    @PutMapping("{vehicleId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<VehicleResponse> update(@PathVariable UUID vehicleId,
        @RequestBody VehicleUpdateRequest request)
    {
        processVehicleUpdateRequestCmd.withRequest(ProcessVehicleUpdateRequestCmd.Request.builder()
                .vehicleId(vehicleId)
                .vehicleUpdateRequest(request)
                .build())
            .execute();

        return SingleResponse.<VehicleResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Vehicle updated successfully")
            .build();
    }

    @Operation(summary = "este api es de prueba para web socket")
    @GetMapping("/test-websocket")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse testWebSocket() {
        webSocketHandler.emitMessage(
            WebSocketTopic.VEHICLE_CREATED, WebSocketSingleResponse.<Vehicle>builder()
                .topic(WebSocketTopic.VEHICLE_CREATED)
                .timestamp(ZonedDateTime.now())
                .data(new Vehicle())
                .build()
        );

        return MessageResponse.builder()
            .code(HttpStatus.OK.value())
            .message("Todo fue exitoso!")
            .build();
    }

    @MessageMapping(WebSocketTopic.TEST_MESSAGE)
    public void handleTestMessage(String message) {
        log.info("FROM WEB SOCKET");
        log.info("Received WebSocket message: {}", message);
    }
}
