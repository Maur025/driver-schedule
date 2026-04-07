package com.kernotec.driverscheduleservice.rest.command.schedule.schedule.transportation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.schedule.cancel.reason.CancelReasonCreateCmd;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.ScheduleTransportationGetDtoCmd;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.ScheduleTransportationUpdateCmd;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.log.ScheduleTransportationLogCreateCmd;
import com.kernotec.driverscheduleservice.exception.schedule.ScheduleTransportationException;
import com.kernotec.driverscheduleservice.jpa.dto.schedule.ScheduleTransportationDto;
import com.kernotec.driverscheduleservice.jpa.dto.schedule.ScheduleTransportationStateDto;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.enums.schedule.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationService;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationStateService;
import com.kernotec.driverscheduleservice.rest.dto.schedule.request.schedule.transportation.ScheduleTransportationCancelRequest;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.rest.dto.common.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessScheduleTransportationCancelRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessScheduleTransportationCancelRequestCmd.Request, Void>
{

    private final ScheduleTransportationStateService scheduleTransportationStateService;
    private final ScheduleTransportationService scheduleTransportationService;

    private final ScheduleTransportationResponseMapper scheduleTransportationResponseMapper;

    private final ScheduleTransportationGetDtoCmd scheduleTransportationGetDtoCmd;
    private final ScheduleTransportationUpdateCmd scheduleTransportationUpdateCmd;
    private final CancelReasonCreateCmd cancelReasonCreateCmd;
    private final ScheduleTransportationLogCreateCmd scheduleTransportationLogCreateCmd;
    private final WebSocketHandler webSocketHandler;

    @Override
    protected void validate(Request request) {
        ScheduleTransportationDto scheduleTransportationDto = scheduleTransportationGetDtoCmd.withRequest(
                ScheduleTransportationGetDtoCmd.Request.builder()
                    .scheduleTransportationId(request.scheduleTransportationId)
                    .build())
            .execute();

        ScheduleTransportationStateDto scheduleTransportationStateDto = scheduleTransportationDto.getScheduleTransportationState();

        if (ScheduleTransportationStateEnum.CANCELLED.equals(
            ScheduleTransportationStateEnum.fromValue(scheduleTransportationStateDto.getCode())))
        {
            throw new ScheduleTransportationException(
                "already.cancelled", "'" + request.scheduleTransportationId + "'",
                HttpStatus.CONFLICT.value()
            );
        }
    }

    @Override
    protected Void run(Request request) {
        ScheduleTransportationCancelRequest scheduleTransportationCancelRequest = request.scheduleTransportationCancelRequest;

        UUID scheduleTransportationStateCancelledId = scheduleTransportationStateService.findIdByCodeThrow(
            ScheduleTransportationStateEnum.CANCELLED);

        scheduleTransportationUpdateCmd.withRequest(
                ScheduleTransportationUpdateCmd.Request.builder()
                    .scheduleTransportationId(request.scheduleTransportationId)
                    .scheduleTransportationStateId(scheduleTransportationStateCancelledId)
                    .build())
            .execute();

        cancelReasonCreateCmd.withRequest(CancelReasonCreateCmd.Request.builder()
                .reasonId(scheduleTransportationCancelRequest.getReasonId())
                .scheduleTransportationId(request.scheduleTransportationId)
                .otherReason(scheduleTransportationCancelRequest.getOtherReason())
                .build())
            .execute();

        scheduleTransportationLogCreateCmd.withRequest(
                ScheduleTransportationLogCreateCmd.Request.builder()
                    .scheduleTransportationId(request.scheduleTransportationId)
                    .scheduleTransportationStateId(scheduleTransportationStateCancelledId)
                    .build())
            .execute();

        emitSocketMessage(request.scheduleTransportationId);

        return null;
    }

    private void emitSocketMessage(UUID scheduleTransportationId)
    {
        ScheduleTransportation scheduleTransportation = scheduleTransportationService.findByIdThrow(
            scheduleTransportationId);

        var socketResponse = WebSocketSingleResponse.<ScheduleTransportationResponse>builder()
            .timestamp(ZonedDateTime.now())
            .data(scheduleTransportationResponseMapper.toResponse(scheduleTransportation));

        webSocketHandler.emitMessage(
            WebSocketTopic.SCHEDULE_TRANSPORTATION_CANCELLED,
            socketResponse.topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_CANCELLED)
                .build()
        );

        ScheduleTransportationDto scheduleTransportationDto = scheduleTransportationGetDtoCmd.withRequest(
                ScheduleTransportationGetDtoCmd.Request.builder()
                    .scheduleTransportationId(scheduleTransportationId)
                    .build())
            .execute();

        webSocketHandler.emitMessageToUser(
            scheduleTransportationDto.getPersonRequested()
                .getUserId(), WebSocketTopic.SCHEDULE_TRANSPORTATION_CANCELLED_TO_USER,
            socketResponse.topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_CANCELLED_TO_USER)
                .build()
        );
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId,
                          @NotNull @Valid ScheduleTransportationCancelRequest scheduleTransportationCancelRequest)
    {

    }
}
