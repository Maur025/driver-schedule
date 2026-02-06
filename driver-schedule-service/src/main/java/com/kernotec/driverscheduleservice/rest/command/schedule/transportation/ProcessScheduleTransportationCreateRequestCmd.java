package com.kernotec.driverscheduleservice.rest.command.schedule.transportation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.schedule.transportation.ScheduleTransportationCreateCmd;
import com.kernotec.driverscheduleservice.command.schedule.transportation.log.ScheduleTransportationLogCreateCmd;
import com.kernotec.driverscheduleservice.command.transportation.request.TransportationRequestGetDtoCmd;
import com.kernotec.driverscheduleservice.command.transportation.request.TransportationRequestUpdateCmd;
import com.kernotec.driverscheduleservice.command.transportation.request.log.TransportationRequestLogCreateCmd;
import com.kernotec.driverscheduleservice.command.trip.assignment.TripAssignmentManyCreateCmd;
import com.kernotec.driverscheduleservice.exception.ScheduleTransportationException;
import com.kernotec.driverscheduleservice.jpa.dto.TransportationRequestDto;
import com.kernotec.driverscheduleservice.jpa.dto.TransportationRequestStateDto;
import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.ScheduleTransportationService;
import com.kernotec.driverscheduleservice.jpa.service.ScheduleTransportationStateService;
import com.kernotec.driverscheduleservice.jpa.service.TransportationRequestStateService;
import com.kernotec.driverscheduleservice.rest.dto.request.schedule.transportation.ScheduleTransportationCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.request.trip.assignment.TripAssignmentCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.request.trip.assignment.TripAssignmentEntityMapper;
import com.kernotec.driverscheduleservice.rest.mapper.response.schedule.transportation.ScheduleTransportationResponseMapper;
import com.kernotec.driverscheduleservice.util.ScheduleTransportationUtil;
import com.kernotec.driverscheduleservice.util.ZonedDateTimeUtil;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessScheduleTransportationCreateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessScheduleTransportationCreateRequestCmd.Request, UUID>
{

    private final ScheduleTransportationService scheduleTransportationService;
    private final ScheduleTransportationStateService scheduleTransportationStateService;
    private final TransportationRequestStateService transportationRequestStateService;

    private final ScheduleTransportationResponseMapper scheduleTransportationResponseMapper;

    private final TransportationRequestGetDtoCmd transportationRequestGetDtoCmd;
    private final ScheduleTransportationCreateCmd scheduleTransportationCreateCmd;
    private final TransportationRequestUpdateCmd transportationRequestUpdateCmd;
    private final ScheduleTransportationDateValidationCmd scheduleTransportationDateValidationCmd;
    private final WebSocketHandler webSocketHandler;
    private final ZonedDateTimeUtil zonedDateTimeUtil;
    private final ScheduleTransportationLogCreateCmd scheduleTransportationLogCreateCmd;
    private final TransportationRequestLogCreateCmd transportationRequestLogCreateCmd;
    private final TripAssignmentEntityMapper tripAssignmentEntityMapper;
    private final TripAssignmentManyCreateCmd tripAssignmentManyCreateCmd;
    private final ScheduleTransportationUtil scheduleTransportationUtil;

    @Override
    protected void validate(Request request) {
        ScheduleTransportationCreateRequest scheduleTransportationCreateRequest = request.scheduleTransportationCreateRequest;

        List<UUID> vehicleIds = scheduleTransportationCreateRequest.getTripAssignments()
            .stream()
            .map(TripAssignmentCreateRequest::getVehicleId)
            .toList();

        List<UUID> driverIds = scheduleTransportationCreateRequest.getTripAssignments()
            .stream()
            .map(TripAssignmentCreateRequest::getDriverId)
            .toList();

        scheduleTransportationDateValidationCmd.withRequest(
                ScheduleTransportationDateValidationCmd.Request.builder()
                    .vehicleIdList(vehicleIds)
                    .driverIdList(driverIds)
                    .requestedDate(scheduleTransportationCreateRequest.getRequestedDate())
                    .requestedStartTime(scheduleTransportationCreateRequest.getRequestedStartTime())
                    .requestedEndTime(scheduleTransportationCreateRequest.getRequestedEndTime())
                    .zoneId(scheduleTransportationCreateRequest.getZoneId())
                    .build())
            .execute();
    }

    @Override
    protected UUID run(Request request) {
        ScheduleTransportationCreateRequest scheduleTransportationCreateRequest = request.scheduleTransportationCreateRequest;

        TransportationRequestDto transportationRequestDto = transportationRequestGetDtoCmd.withRequest(
                TransportationRequestGetDtoCmd.Request.builder()
                    .transportationRequestId(
                        scheduleTransportationCreateRequest.getTransportationRequestId())
                    .build())
            .execute();

        TransportationRequestStateDto transportationRequestStateDto = transportationRequestDto.getTransportationRequestState();

        if (!transportationRequestStateDto.getCode()
            .equals(String.valueOf(TransportationRequestStateEnum.REQUESTED)))
        {
            throw new ScheduleTransportationException(
                "invalid.state.to.action", "'" + transportationRequestStateDto.getCode() + "'",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        UUID transportationRequestStateApprovedId = transportationRequestStateService.findIdByCodeThrow(
            TransportationRequestStateEnum.APPROVED);

        transportationRequestUpdateCmd.withRequest(TransportationRequestUpdateCmd.Request.builder()
                .transportationRequestId(
                    scheduleTransportationCreateRequest.getTransportationRequestId())
                .transportationRequestStateId(transportationRequestStateApprovedId)
                .build())
            .execute();

        transportationRequestLogCreateCmd.withRequest(
                TransportationRequestLogCreateCmd.Request.builder()
                    .transportationRequestId(
                        scheduleTransportationCreateRequest.getTransportationRequestId())
                    .transportationRequestStateId(transportationRequestStateApprovedId)
                    .build())
            .execute();

        UUID scheduledTransportationStateScheduledId = scheduleTransportationStateService.findIdByCodeThrow(
            ScheduleTransportationStateEnum.SCHEDULED);

        ZonedDateTime scheduledFrom = zonedDateTimeUtil.getNewOfDateAndTime(
            scheduleTransportationCreateRequest.getRequestedDate(),
            scheduleTransportationCreateRequest.getRequestedStartTime(),
            scheduleTransportationCreateRequest.getZoneId()
        );

        ZonedDateTime scheduledTo = zonedDateTimeUtil.getNewOfDateAndTime(
            scheduleTransportationCreateRequest.getRequestedDate(),
            scheduleTransportationCreateRequest.getRequestedEndTime(),
            scheduleTransportationCreateRequest.getZoneId()
        );

        UUID scheduleTransportationId = scheduleTransportationCreateCmd.withRequest(
                ScheduleTransportationCreateCmd.Request.builder()
                    .scheduleFrom(scheduledFrom)
                    .scheduleTo(scheduledTo)
                    .scheduledDate(scheduleTransportationCreateRequest.getRequestedDate())
                    .transportationRequestId(
                        scheduleTransportationCreateRequest.getTransportationRequestId())
                    .personRequestedId(transportationRequestDto.getPersonRequestedId())
                    .scheduleTransportationStateId(scheduledTransportationStateScheduledId)
                    .build())
            .execute();

        scheduleTransportationUtil.registryTripAssignments(
            scheduleTransportationCreateRequest.getTripAssignments(), scheduleTransportationId);

        scheduleTransportationLogCreateCmd.withRequest(
                ScheduleTransportationLogCreateCmd.Request.builder()
                    .scheduleTransportationId(scheduleTransportationId)
                    .scheduleTransportationStateId(scheduledTransportationStateScheduledId)
                    .build())
            .execute();

        emitSocketMessage(scheduleTransportationId, transportationRequestDto);

        return scheduleTransportationId;
    }


    private void emitSocketMessage(UUID scheduleTransportationId,
        TransportationRequestDto transportationRequestDto)
    {
        ScheduleTransportation scheduleTransportation = scheduleTransportationService.findByIdThrow(
            scheduleTransportationId);

        var socketResponse = WebSocketSingleResponse.<ScheduleTransportationResponse>builder()
            .timestamp(ZonedDateTime.now())
            .data(scheduleTransportationResponseMapper.toResponse(scheduleTransportation));

        webSocketHandler.emitMessage(
            WebSocketTopic.SCHEDULE_TRANSPORTATION_CREATED,
            socketResponse.topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_CREATED)
                .build()
        );

        webSocketHandler.emitMessageToUser(
            transportationRequestDto.getPersonRequested()
                .getUserId(), WebSocketTopic.SCHEDULE_TRANSPORTATION_CREATED_TO_USER,
            socketResponse.topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_CREATED_TO_USER)
                .build()
        );
    }

    @Builder
    public record Request(
        @NotNull ScheduleTransportationCreateRequest scheduleTransportationCreateRequest)
    {

    }
}
