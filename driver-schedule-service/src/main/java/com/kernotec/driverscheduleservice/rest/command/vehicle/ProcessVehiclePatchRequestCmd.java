package com.kernotec.driverscheduleservice.rest.command.vehicle;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.core.exception.custom.base.DefaultMultipleException;
import com.kernotec.driverscheduleservice.command.vehicle.VehicleUpdateCmd;
import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.entity.Vehicle;
import com.kernotec.driverscheduleservice.jpa.service.ScheduleTransportationService;
import com.kernotec.driverscheduleservice.jpa.service.VehicleService;
import com.kernotec.driverscheduleservice.rest.dto.request.vehicle.VehiclePatchRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.vehicle.VehicleResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.schedule.transportation.ScheduleTransportationResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.response.vehicle.VehicleResponseMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessVehiclePatchRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessVehiclePatchRequestCmd.Request, Void>
{

    private final VehicleService vehicleService;
    private final VehicleResponseMapper vehicleResponseMapper;

    private final VehicleUpdateCmd vehicleUpdateCmd;
    private final WebSocketHandler webSocketHandler;
    private final ScheduleTransportationService scheduleTransportationService;
    private final ScheduleTransportationResponseMapper scheduleTransportationResponseMapper;

    @Override
    protected void validate(Request request) {
        VehiclePatchRequest vehiclePatchRequest = request.vehiclePatchRequest;

        if (vehiclePatchRequest.getIsEnabled() == null || vehiclePatchRequest.getIsEnabled()) {
            return;
        }

        Page<ScheduleTransportation> scheduleTransportationPage = scheduleTransportationService.findWhichVehicleBusy(
            request.vehicleId);

        if (scheduleTransportationPage.getContent()
            .isEmpty())
        {
            return;
        }

        List<ScheduleTransportationResponse> scheduleTransportationResponseList = scheduleTransportationResponseMapper.toResponse(
            scheduleTransportationPage.getContent());

        List<Map<String, String>> errorList = getErrorListOfResponseData(
            scheduleTransportationResponseList);

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
        List<ScheduleTransportationResponse> scheduleTransportationResponseList)
    {
        List<Map<String, String>> errors = new ArrayList<>();
        String messageKey = "expection.vehicle.request.pending.error.message";

        for (ScheduleTransportationResponse scheduleResponse : scheduleTransportationResponseList) {
            String requestNumber = scheduleResponse.getTransportationRequest()
                .getCorrelative()
                .toString();

            String requestId = scheduleResponse.getTransportationRequest()
                .getCode();

            String state = scheduleResponse.getScheduleTransportationState()
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
