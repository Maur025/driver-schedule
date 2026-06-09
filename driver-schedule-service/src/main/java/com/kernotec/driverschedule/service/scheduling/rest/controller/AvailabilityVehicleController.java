package com.kernotec.driverschedule.service.scheduling.rest.controller;

import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.driverschedule.resource.authorize.annotation.CanReadVehicle;
import com.kernotec.driverschedule.resource.authorize.annotation.CanUpdateVehicle;
import com.kernotec.driverschedule.resource.exception.VehicleException;
import com.kernotec.driverschedule.resource.jpa.entity.Vehicle;
import com.kernotec.driverschedule.resource.jpa.service.VehicleService;
import com.kernotec.driverschedule.resource.rest.ResourceApiSpec.VehicleSpec;
import com.kernotec.driverschedule.resource.rest.dto.request.VehiclePatchRequest;
import com.kernotec.driverschedule.resource.rest.dto.response.VehicleResponse;
import com.kernotec.driverschedule.service.scheduling.jpa.service.AvailabilityForAssignmentService;
import com.kernotec.driverschedule.service.scheduling.rest.command.schedule.ProcessVehiclePatchRequestCmd;
import com.kernotec.driverschedule.service.scheduling.rest.dto.request.schedule.AvailabilityForAssignmentRequest;
import com.kernotec.driverschedule.service.scheduling.rest.dto.request.schedule.VehicleScheduleConflictRequest;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.schedule.AvailabilityForAssignmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = VehicleSpec.TAG_NAME, description = VehicleSpec.TAG_DESCRIPTION)
@RequestMapping(path = VehicleSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class AvailabilityVehicleController {

    private final VehicleService vehicleService;
    private final AvailabilityForAssignmentService availabilityForAssignmentService;
    private final ProcessVehiclePatchRequestCmd processVehiclePatchRequestCmd;

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
}
