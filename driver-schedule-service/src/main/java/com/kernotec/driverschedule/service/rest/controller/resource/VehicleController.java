package com.kernotec.driverschedule.service.rest.controller.resource;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.MessageResponse;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.common.csv.imports.CsvImportCmd;
import com.kernotec.driverschedule.common.response.LookupResponse;
import com.kernotec.driverschedule.common.response.WebSocketSingleResponse;
import com.kernotec.driverschedule.common.rest.ApiSpec.VehicleSpec;
import com.kernotec.driverschedule.service.common.annotation.vehicle.CanCreateVehicle;
import com.kernotec.driverschedule.service.common.annotation.vehicle.CanReadVehicle;
import com.kernotec.driverschedule.service.common.annotation.vehicle.CanUpdateVehicle;
import com.kernotec.driverschedule.service.exception.resource.VehicleException;
import com.kernotec.driverschedule.service.jpa.entity.resource.Vehicle;
import com.kernotec.driverschedule.service.jpa.service.resource.AvailabilityForAssignmentService;
import com.kernotec.driverschedule.service.jpa.service.resource.VehicleService;
import com.kernotec.driverschedule.service.rest.command.resource.vehicle.ProcessVehicleCreateRequestCmd;
import com.kernotec.driverschedule.service.rest.command.resource.vehicle.ProcessVehiclePatchRequestCmd;
import com.kernotec.driverschedule.service.rest.command.resource.vehicle.ProcessVehicleUpdateRequestCmd;
import com.kernotec.driverschedule.service.rest.command.resource.vehicle.VehicleCsvImportGetDtoCmd;
import com.kernotec.driverschedule.service.rest.command.resource.vehicle.VehicleCsvImportSaveCmd;
import com.kernotec.driverschedule.service.rest.dto.resource.VehicleCsvImportDto;
import com.kernotec.driverschedule.service.rest.dto.resource.request.AvailabilityForAssignmentRequest;
import com.kernotec.driverschedule.service.rest.dto.resource.request.vehicle.VehicleCreateRequest;
import com.kernotec.driverschedule.service.rest.dto.resource.request.vehicle.VehiclePatchRequest;
import com.kernotec.driverschedule.service.rest.dto.resource.request.vehicle.VehicleScheduleConflictRequest;
import com.kernotec.driverschedule.service.rest.dto.resource.request.vehicle.VehicleUpdateRequest;
import com.kernotec.driverschedule.service.rest.dto.resource.response.AvailabilityForAssignmentResponse;
import com.kernotec.driverschedule.service.rest.dto.resource.response.vehicle.VehicleLookupResponse;
import com.kernotec.driverschedule.service.rest.dto.resource.response.vehicle.VehicleResponse;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.vehicle.VehicleResponseMapper;
import com.kernotec.driverschedule.socket.WebSocketTopic;
import com.kernotec.driverschedule.socket.service.WebSocketHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Tag(name = VehicleSpec.TAG_NAME, description = VehicleSpec.TAG_DESCRIPTION)
@RequestMapping(path = VehicleSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    private final VehicleResponseMapper vehicleResponseMapper;

    private final ProcessVehicleCreateRequestCmd processVehicleCreateRequestCmd;
    private final ProcessVehicleUpdateRequestCmd processVehicleUpdateRequestCmd;
    private final WebSocketHandler webSocketHandler;
    private final CsvImportCmd<VehicleCsvImportDto> csvImportCmd;
    private final VehicleCsvImportGetDtoCmd vehicleCsvImportGetDtoCmd;
    private final VehicleCsvImportSaveCmd vehicleCsvImportSaveCmd;
    private final ProcessVehiclePatchRequestCmd processVehiclePatchRequestCmd;
    private final AvailabilityForAssignmentService availabilityForAssignmentService;

    @Operation(summary = "find all vehicles")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CanReadVehicle
    public PageResponse<VehicleResponse> findAll(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "descending", defaultValue = "true") boolean descending)
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
    @CanReadVehicle
    public PageResponse<VehicleResponse> findAllWithoutPagination() {
        Pageable pageable = PageableUtil.of(0, 20, "vehicleNumber", false);

        Page<Vehicle> vehiclePage = vehicleService.findAll(pageable);

        return PageResponse.<VehicleResponse>builder()
            .code(HttpStatus.OK.value())
            .data(vehicleResponseMapper.toResponse(vehiclePage.getContent()))
            .build();
    }

    @Operation(summary = "find by id")
    @GetMapping("{vehicleId}")
    @ResponseStatus(HttpStatus.OK)
    @CanReadVehicle
    public SingleResponse<VehicleResponse> findById(@PathVariable("vehicleId") UUID vehicleId)
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
    @CanCreateVehicle
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
    @CanUpdateVehicle
    public SingleResponse<VehicleResponse> update(@PathVariable("vehicleId") UUID vehicleId,
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

    @Operation(summary = "find vehicle schedule conflicts")
    @PostMapping("{vehicleId}/schedule-conflicts")
    @ResponseStatus(HttpStatus.OK)
    @CanReadVehicle
    public SingleResponse<AvailabilityForAssignmentResponse> findVehicleScheduleConflicts(
        @PathVariable("vehicleId") UUID vehicleId,
        @RequestBody VehicleScheduleConflictRequest request)
    {
        List<Vehicle> vehicleNotUsableList = vehicleService.findCanNotUsed(Set.of(vehicleId));

        if (!vehicleNotUsableList.isEmpty()) {
            throw new VehicleException(
                "not.usable", "'" + vehicleId + "'", HttpStatus.CONFLICT.value());
        }

        AvailabilityForAssignmentResponse availabilityForAssignmentResponse = availabilityForAssignmentService.checkVehicleIsAvailable(
            AvailabilityForAssignmentRequest.builder()
                .vehicleIds(Set.of(vehicleId))
                .dateFrom(request.getConflictValidationFrom())
                .dateTo(request.getConflictValidationTo())
                .zoneId(request.getZoneId())
                .scheduleTransportationExcludeId(request.getScheduleTransportationExcludeId())
                .build());

        return SingleResponse.<AvailabilityForAssignmentResponse>builder()
            .code(HttpStatus.OK.value())
            .data(availabilityForAssignmentResponse)
            .build();
    }

    @Operation(summary = "import vehicles of csv file")
    @PostMapping(value = "imports/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @CanCreateVehicle
    public MessageResponse importVehiclesFromExcel(
        @RequestPart(value = "file") MultipartFile multipartFile)
    {
        csvImportCmd.withRequest(CsvImportCmd.Request.<VehicleCsvImportDto>builder()
                .excelFile(multipartFile)
                .mapperCallback(csvData -> vehicleCsvImportGetDtoCmd.withRequest(
                        VehicleCsvImportGetDtoCmd.Request.builder()
                            .csvData(csvData)
                            .build())
                    .execute())
                .saveCallback(dtoList -> vehicleCsvImportSaveCmd.withRequest(
                        VehicleCsvImportSaveCmd.Request.builder()
                            .vehicleCsvImportDtoList(dtoList)
                            .build())
                    .execute())
                .build())
            .execute();

        return MessageResponse.builder()
            .code(HttpStatus.OK.value())
            .message("Vehicles imported successfully")
            .build();
    }

    @Operation(summary = "patch update vehicle")
    @PatchMapping("{vehicleId}")
    @ResponseStatus(HttpStatus.OK)
    @CanUpdateVehicle
    public SingleResponse<VehicleResponse> patchUpdate(@PathVariable("vehicleId") UUID vehicleId,
        @RequestBody VehiclePatchRequest request)
    {
        processVehiclePatchRequestCmd.withRequest(ProcessVehiclePatchRequestCmd.Request.builder()
                .vehicleId(vehicleId)
                .vehiclePatchRequest(request)
                .build())
            .execute();

        return SingleResponse.<VehicleResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Vehicle patched successfully")
            .build();
    }

    @Operation(summary = "find all to lookup")
    @GetMapping("lookup")
    @ResponseStatus(HttpStatus.OK)
    public LookupResponse<List<VehicleLookupResponse>> findAllToLookup(
        @RequestParam(name = "keyword", required = false) String keyword)
    {
        Pageable pageable = PageableUtil.of(0, 25, "vehicleNumber", false);
        Page<VehicleLookupResponse> vehicleLookupResponsePage = vehicleService.findAllToLookup(
            keyword, pageable);

        return LookupResponse.<List<VehicleLookupResponse>>builder()
            .code(HttpStatus.OK.value())
            .data(vehicleLookupResponsePage.getContent())
            .build();
    }
}
