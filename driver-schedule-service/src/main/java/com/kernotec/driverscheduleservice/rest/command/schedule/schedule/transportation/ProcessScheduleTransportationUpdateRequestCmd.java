package com.kernotec.driverscheduleservice.rest.command.schedule.schedule.transportation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.schedule.reschedule.reason.RescheduleReasonCreateCmd;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.ScheduleTransportationGetDtoCmd;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.ScheduleTransportationUpdateCmd;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.log.ScheduleTransportationLogCreateCmd;
import com.kernotec.driverscheduleservice.exception.schedule.ScheduleTransportationException;
import com.kernotec.driverscheduleservice.jpa.dto.schedule.ScheduleTransportationDto;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.enums.schedule.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationService;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationStateService;
import com.kernotec.driverscheduleservice.jpa.service.schedule.TripAssignmentService;
import com.kernotec.driverscheduleservice.rest.dto.common.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.dto.schedule.request.schedule.transportation.ScheduleTransportationUpdateRequest;
import com.kernotec.driverscheduleservice.rest.dto.schedule.request.trip.assignment.TripAssignmentCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationResponseMapper;
import com.kernotec.driverscheduleservice.util.ScheduleTransportationUtil;
import com.kernotec.driverscheduleservice.util.ScheduleTransportationUtil.RegistryTripAssignmentRequest;
import com.kernotec.driverscheduleservice.util.ZonedDateTimeUtil;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.Valid;
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
public class ProcessScheduleTransportationUpdateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessScheduleTransportationUpdateRequestCmd.Request, Void>
{

    private final ScheduleTransportationStateService scheduleTransportationStateService;
    private final ScheduleTransportationService scheduleTransportationService;

    private final ScheduleTransportationResponseMapper scheduleTransportationResponseMapper;

    private final ScheduleTransportationDateValidationCmd scheduleTransportationDateValidationCmd;
    private final ScheduleTransportationUpdateCmd scheduleTransportationUpdateCmd;
    private final RescheduleReasonCreateCmd rescheduleReasonCreateCmd;
    private final ScheduleTransportationGetDtoCmd scheduleTransportationGetDtoCmd;
    private final ScheduleTransportationLogCreateCmd scheduleTransportationLogCreateCmd;
    private final ZonedDateTimeUtil zonedDateTimeUtil;
    private final WebSocketHandler webSocketHandler;
    private final TripAssignmentService tripAssignmentService;
    private final ScheduleTransportationUtil scheduleTransportationUtil;

    @Override
    protected void validate(Request request) {
        ScheduleTransportationUpdateRequest scheduleTransportationUpdateRequest = request.scheduleTransportationUpdateRequest;

        ScheduleTransportationDto scheduleTransportationDto = scheduleTransportationGetDtoCmd.withRequest(
                ScheduleTransportationGetDtoCmd.Request.builder()
                    .scheduleTransportationId(request.scheduleTransportationId)
                    .build())
            .execute();

        ScheduleTransportationStateEnum scheduleStateCurrent = ScheduleTransportationStateEnum.fromValue(
            scheduleTransportationDto.getScheduleTransportationState()
                .getCode());

        if (!scheduleStateCurrent.canTransitionTo(ScheduleTransportationStateEnum.RESCHEDULED)) {
            throw new ScheduleTransportationException(
                "invalid.state.to.action",
                "'" + scheduleStateCurrent + "'", HttpStatus.CONFLICT.value()
            );
        }

        List<UUID> vehicleIds = scheduleTransportationUpdateRequest.getTripAssignments()
            .stream()
            .map(TripAssignmentCreateRequest::getVehicleId)
            .toList();

        List<UUID> driverIds = scheduleTransportationUpdateRequest.getTripAssignments()
            .stream()
            .map(TripAssignmentCreateRequest::getDriverId)
            .toList();

        scheduleTransportationDateValidationCmd.withRequest(
                ScheduleTransportationDateValidationCmd.Request.builder()
                    .vehicleIdList(vehicleIds)
                    .driverIdList(driverIds)
                    .requestedStartTime(scheduleTransportationUpdateRequest.getRequestedStartTime())
                    .requestedEndTime(scheduleTransportationUpdateRequest.getRequestedEndTime())
                    .zoneId(scheduleTransportationUpdateRequest.getZoneId())
                    .scheduleTransportationExcludeId(request.scheduleTransportationId)
                    .build())
            .execute();
    }

    @Override
    protected Void run(Request request) {
        ScheduleTransportationUpdateRequest scheduleTransportationUpdateRequest = request.scheduleTransportationUpdateRequest;

        UUID scheduleTransportationStateRescheduledId = scheduleTransportationStateService.findIdByCodeThrow(
            ScheduleTransportationStateEnum.RESCHEDULED);

        ZonedDateTime scheduledFrom = zonedDateTimeUtil.getDateScheduleNormalized(
            scheduleTransportationUpdateRequest.getRequestedStartTime());

        ZonedDateTime scheduledTo = zonedDateTimeUtil.getDateScheduleNormalized(
            scheduleTransportationUpdateRequest.getRequestedEndTime());

        scheduleTransportationUpdateCmd.withRequest(
                ScheduleTransportationUpdateCmd.Request.builder()
                    .scheduleTransportationId(request.scheduleTransportationId)
                    .scheduleFrom(scheduledFrom)
                    .scheduleTo(scheduledTo)
                    .requestedDate(scheduledFrom)
                    .scheduleTransportationStateId(scheduleTransportationStateRescheduledId)
                    .build())
            .execute();

        tripAssignmentService.deleteAllByScheduleTransportationId(request.scheduleTransportationId);

        scheduleTransportationUtil.registryTripAssignments(RegistryTripAssignmentRequest.builder()
            .tripAssignmentCreateRequestList(
                scheduleTransportationUpdateRequest.getTripAssignments())
            .scheduleTransportationId(request.scheduleTransportationId)
            .estimatedStartTime(scheduledFrom)
            .estimatedEndTime(scheduledTo)
            .build());

        scheduleTransportationLogCreateCmd.withRequest(
                ScheduleTransportationLogCreateCmd.Request.builder()
                    .scheduleTransportationId(request.scheduleTransportationId)
                    .scheduleTransportationStateId(scheduleTransportationStateRescheduledId)
                    .build())
            .execute();

        rescheduleReasonCreateCmd.withRequest(RescheduleReasonCreateCmd.Request.builder()
                .reasonId(scheduleTransportationUpdateRequest.getReasonId())
                .scheduleTransportationId(request.scheduleTransportationId)
                .otherReason(scheduleTransportationUpdateRequest.getOtherReason())
                .build())
            .execute();

        emitSocketMessage(request.scheduleTransportationId);

        return null;
    }

    private void emitSocketMessage(UUID scheduleTransportationId) {
        ScheduleTransportation scheduleTransportation = scheduleTransportationService.findByIdThrow(
            scheduleTransportationId);

        var socketResponse = WebSocketSingleResponse.<ScheduleTransportationResponse>builder()
            .timestamp(ZonedDateTime.now())
            .data(scheduleTransportationResponseMapper.toResponse(scheduleTransportation));

        webSocketHandler.emitMessage(
            WebSocketTopic.SCHEDULE_TRANSPORTATION_RESCHEDULED,
            socketResponse.topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_RESCHEDULED)
                .build()
        );

        ScheduleTransportationDto scheduleTransportationDto = scheduleTransportationGetDtoCmd.withRequest(
                ScheduleTransportationGetDtoCmd.Request.builder()
                    .scheduleTransportationId(scheduleTransportationId)
                    .build())
            .execute();

        webSocketHandler.emitMessageToUser(
            scheduleTransportationDto.getPersonRequested()
                .getUserId(), WebSocketTopic.SCHEDULE_TRANSPORTATION_RESCHEDULED_TO_USER,
            socketResponse.topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_RESCHEDULED_TO_USER)
                .build()
        );
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId,
                          @NotNull @Valid ScheduleTransportationUpdateRequest scheduleTransportationUpdateRequest)
    {

    }
}
