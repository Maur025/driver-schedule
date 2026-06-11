package com.kernotec.driverschedule.service.scheduling.rest.command.schedule;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.common.util.ScheduleTransportationUtil;
import com.kernotec.driverschedule.service.scheduling.command.schedule.CancelReasonCreateCmd;
import com.kernotec.driverschedule.service.scheduling.command.schedule.ScheduleTransportationGetDtoCmd;
import com.kernotec.driverschedule.service.scheduling.command.schedule.ScheduleTransportationLogCreateCmd;
import com.kernotec.driverschedule.service.scheduling.command.schedule.ScheduleTransportationUpdateCmd;
import com.kernotec.driverschedule.service.scheduling.jpa.dto.ScheduleTransportationDto;
import com.kernotec.driverschedule.service.scheduling.jpa.dto.TripAssignmentDto;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverschedule.service.scheduling.jpa.service.ScheduleTransportationStateService;
import com.kernotec.driverschedule.service.scheduling.notification.DriverAssignmentPushNotification;
import com.kernotec.driverschedule.service.scheduling.notification.SchedulePushNotification;
import com.kernotec.driverschedule.service.scheduling.rest.dto.request.schedule.ScheduleTransportationCancelRequest;
import com.kernotec.driverschedule.service.scheduling.socket.ScheduleSocketTopic;
import com.kernotec.driverschedule.service.scheduling.socket.ScheduleTransportationSocketHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
    private final ScheduleTransportationSocketHandler scheduleTransportationSocketHandler;
    private final SchedulePushNotification schedulePushNotification;
    private final DriverAssignmentPushNotification driverAssignmentPushNotification;

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

        handleMessagesEmit(request.scheduleTransportationId);

        return null;
    }

    private void handleMessagesEmit(UUID scheduleTransportationId)
    {
        ScheduleTransportationDto scheduleTransportationDto = scheduleTransportationGetDtoCmd.withRequest(
                ScheduleTransportationGetDtoCmd.Request.builder()
                    .scheduleTransportationId(scheduleTransportationId)
                    .build())
            .execute();

        Set<UUID> driverIds = scheduleTransportationDto.getTripAssignments()
            .stream()
            .map(TripAssignmentDto::getDriverId)
            .collect(Collectors.toSet());

        schedulePushNotification.onCancelled(
            scheduleTransportationId, Set.of(scheduleTransportationDto.getPersonRequestedId()));

        driverAssignmentPushNotification.onCancelled(scheduleTransportationId, driverIds);

        UUID userToEmit = scheduleTransportationDto.getPersonRequested()
            .getUserId();

        scheduleTransportationSocketHandler.emitMessage(
            ScheduleTransportationSocketHandler.Request.builder()
                .scheduleTransportationId(scheduleTransportationId)
                .topic(ScheduleSocketTopic.SCHEDULE_TRANSPORTATION_CANCELLED)
                .build());

        scheduleTransportationSocketHandler.emitMessage(
            ScheduleTransportationSocketHandler.Request.builder()
                .scheduleTransportationId(scheduleTransportationId)
                .topic(ScheduleSocketTopic.SCHEDULE_TRANSPORTATION_CANCELLED_TO_USER)
                .toList(Set.of(userToEmit))
                .build());
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId,
                          @NotNull @Valid ScheduleTransportationCancelRequest scheduleTransportationCancelRequest)
    {

    }
}
