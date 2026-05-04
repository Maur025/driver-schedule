package com.kernotec.driverscheduleservice.rest.command.schedule.schedule.transportation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.schedule.cancel.reason.CancelReasonCreateCmd;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.ScheduleTransportationGetDtoCmd;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.ScheduleTransportationUpdateCmd;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.log.ScheduleTransportationLogCreateCmd;
import com.kernotec.driverscheduleservice.jpa.dto.schedule.ScheduleTransportationDto;
import com.kernotec.driverscheduleservice.jpa.enums.schedule.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationStateService;
import com.kernotec.driverscheduleservice.notification.dto.NotificationSendRequest;
import com.kernotec.driverscheduleservice.notification.service.NotificationOrchestrator;
import com.kernotec.driverscheduleservice.notification.templates.NotificationTemplate.ScheduleCancelledTemplate;
import com.kernotec.driverscheduleservice.rest.dto.schedule.request.schedule.transportation.ScheduleTransportationCancelRequest;
import com.kernotec.driverscheduleservice.rest.socket.schedule.ScheduleTransportationSocketHandler;
import com.kernotec.driverscheduleservice.util.ScheduleTransportationUtil;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessScheduleTransportationCancelRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessScheduleTransportationCancelRequestCmd.Request, Void>
{

    private final ScheduleTransportationStateService scheduleTransportationStateService;

    private final ScheduleTransportationGetDtoCmd scheduleTransportationGetDtoCmd;
    private final ScheduleTransportationUpdateCmd scheduleTransportationUpdateCmd;
    private final CancelReasonCreateCmd cancelReasonCreateCmd;
    private final ScheduleTransportationLogCreateCmd scheduleTransportationLogCreateCmd;

    private final ScheduleTransportationUtil scheduleTransportationUtil;
    private final NotificationOrchestrator notificationOrchestrator;
    private final ScheduleTransportationSocketHandler scheduleTransportationSocketHandler;

    @Override
    protected void validate(Request request) {
        ScheduleTransportationDto scheduleTransportationDto = scheduleTransportationGetDtoCmd.withRequest(
                ScheduleTransportationGetDtoCmd.Request.builder()
                    .scheduleTransportationId(request.scheduleTransportationId)
                    .build())
            .execute();

        scheduleTransportationUtil.validateTransitionOfDto(
            scheduleTransportationDto, ScheduleTransportationStateEnum.CANCELLED);
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
        ScheduleTransportationDto scheduleTransportationDto = scheduleTransportationGetDtoCmd.withRequest(
                ScheduleTransportationGetDtoCmd.Request.builder()
                    .scheduleTransportationId(scheduleTransportationId)
                    .build())
            .execute();

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(ScheduleCancelledTemplate.TITLE)
            .body(ScheduleCancelledTemplate.BODY)
            .campaignRecipient(ScheduleCancelledTemplate.RECEIVER)
            .dataMap(Map.of("screen", "schedule/" + scheduleTransportationId))
            .personIds(Set.of(scheduleTransportationDto.getPersonRequestedId()))
            .build());

        UUID userToEmit = scheduleTransportationDto.getPersonRequested()
            .getUserId();

        scheduleTransportationSocketHandler.emitMessage(
            ScheduleTransportationSocketHandler.Request.builder()
                .scheduleTransportationId(scheduleTransportationId)
                .topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_CANCELLED)
                .build());

        scheduleTransportationSocketHandler.emitMessage(
            ScheduleTransportationSocketHandler.Request.builder()
                .scheduleTransportationId(scheduleTransportationId)
                .topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_CANCELLED_TO_USER)
                .toList(Set.of(userToEmit))
                .build());
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId,
                          @NotNull @Valid ScheduleTransportationCancelRequest scheduleTransportationCancelRequest)
    {

    }
}
