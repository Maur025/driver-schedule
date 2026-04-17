package com.kernotec.driverscheduleservice.rest.command.resource.vehicle;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.core.exception.custom.base.DefaultMultipleException;
import com.kernotec.driverscheduleservice.command.resource.vehicle.VehicleUpdateCmd;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Vehicle;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.TripAssignment;
import com.kernotec.driverscheduleservice.jpa.service.resource.VehicleService;
import com.kernotec.driverscheduleservice.jpa.service.schedule.TripAssignmentService;
import com.kernotec.driverscheduleservice.rest.dto.common.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.dto.resource.request.vehicle.VehiclePatchRequest;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.vehicle.VehicleResponse;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.trip.assignment.TripAssignmentResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.vehicle.VehicleResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.trip.assignment.TripAssignmentResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessVehiclePatchRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessVehiclePatchRequestCmd.Request, Void>
{

    private final VehicleService vehicleService;
    private final VehicleResponseMapper vehicleResponseMapper;

    private final VehicleUpdateCmd vehicleUpdateCmd;
    private final WebSocketHandler webSocketHandler;
    private final TripAssignmentService tripAssignmentService;
    private final TripAssignmentResponseMapper tripAssignmentResponseMapper;

    @Override
    protected void validate(Request request) {
        VehiclePatchRequest vehiclePatchRequest = request.vehiclePatchRequest;

        if (vehiclePatchRequest.getIsEnabled() == null || vehiclePatchRequest.getIsEnabled()) {
            return;
        }

        Page<TripAssignment> tripAssignmentPage = tripAssignmentService.findConflictsToDisableVehicle(
            request.vehicleId);

        if (tripAssignmentPage.isEmpty()) {
            log.debug("No conflicts found for vehicle with id: {}", request.vehicleId);
            return;
        }

        List<TripAssignmentResponse> tripAssignmentResponseList = tripAssignmentResponseMapper.toResponse(
            tripAssignmentPage.getContent());

        List<Map<String, String>> errorList = getErrorListOfResponseData(
            tripAssignmentResponseList);

        throw new DefaultMultipleException(HttpStatus.CONFLICT.value(), errorList);
    }

    @Override
    protected Void run(Request request) {
        VehiclePatchRequest vehiclePatchRequest = request.vehiclePatchRequest;

        vehicleUpdateCmd.withRequest(VehicleUpdateCmd.Request.builder()
                .vehicleId(request.vehicleId)
                .isEnabled(vehiclePatchRequest.getIsEnabled())
                .build())
            .execute();

        Vehicle vehicle = vehicleService.findByIdThrow(request.vehicleId);

        webSocketHandler.emitMessage(
            WebSocketTopic.VEHICLE_UPDATED, WebSocketSingleResponse.<VehicleResponse>builder()
                .topic(WebSocketTopic.VEHICLE_UPDATED)
                .timestamp(ZonedDateTime.now())
                .data(vehicleResponseMapper.toResponse(vehicle))
                .build()
        );

        return null;
    }

    private List<Map<String, String>> getErrorListOfResponseData(
        List<TripAssignmentResponse> scheduleTransportationResponseList)
    {
        List<Map<String, String>> errors = new ArrayList<>();
        String messageKey = "expection.vehicle.request.pending.error.message";

        for (TripAssignmentResponse tripAssignmentResponse : scheduleTransportationResponseList) {
            String requestNumber = tripAssignmentResponse.getScheduleTransportation()
                .getTransportationRequest()
                .getCorrelative()
                .toString();

            String requestId = tripAssignmentResponse.getScheduleTransportation()
                .getTransportationRequest()
                .getCode();

            String state = tripAssignmentResponse.getScheduleTransportation()
                .getScheduleTransportationState()
                .getCode();

            errors.add(getErrorMapToList(messageKey, state, requestNumber, requestId));
        }

        return errors;
    }

    private Map<String, String> getErrorMapToList(String messageKey, String state,
        String requestNumber, String requestId)
    {
        String messageParam = String.format(
            "requestNumber: %s | requestId: %s", requestNumber, requestId);

        return Map.of(
            "propertyPath", state, "messageKey", messageKey, "messageParam",
            messageParam
        );
    }

    @Builder
    public record Request(@NotNull UUID vehicleId,
                          @NotNull @Valid VehiclePatchRequest vehiclePatchRequest)
    {

    }
}
