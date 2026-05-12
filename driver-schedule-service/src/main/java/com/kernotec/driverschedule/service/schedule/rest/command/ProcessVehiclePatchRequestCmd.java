package com.kernotec.driverschedule.service.schedule.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.core.exception.custom.base.DefaultMultipleException;
import com.kernotec.driverschedule.resource.command.VehicleUpdateCmd;
import com.kernotec.driverschedule.resource.rest.dto.request.VehiclePatchRequest;
import com.kernotec.driverschedule.resource.socket.ResourceSocketTopic;
import com.kernotec.driverschedule.resource.socket.VehicleSocketHandler;
import com.kernotec.driverschedule.service.schedule.jpa.entity.TripAssignment;
import com.kernotec.driverschedule.service.schedule.jpa.service.TripAssignmentService;
import com.kernotec.driverschedule.service.schedule.rest.dto.response.TripAssignmentResponse;
import com.kernotec.driverschedule.service.schedule.rest.mapper.response.assignment.TripAssignmentResponseMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

    private final VehicleUpdateCmd vehicleUpdateCmd;
    private final TripAssignmentService tripAssignmentService;
    private final TripAssignmentResponseMapper tripAssignmentResponseMapper;
    private final VehicleSocketHandler vehicleSocketHandler;

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

        vehicleSocketHandler.emitMessage(VehicleSocketHandler.Request.builder()
            .vehicleId(request.vehicleId())
            .topic(ResourceSocketTopic.VEHICLE_UPDATED)
            .build());

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
