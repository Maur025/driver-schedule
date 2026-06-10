package com.kernotec.driverschedule.service.scheduling.rest.command.schedule;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.common.util.ScheduleTransportationUtil;
import com.kernotec.driverschedule.service.scheduling.common.util.ScheduleTransportationUtil.RegistryTripAssignmentRequest;
import com.kernotec.driverschedule.service.scheduling.command.schedule.RescheduleReasonCreateCmd;
import com.kernotec.driverschedule.service.scheduling.command.schedule.ScheduleTransportationGetDtoCmd;
import com.kernotec.driverschedule.service.scheduling.command.schedule.ScheduleTransportationLogCreateCmd;
import com.kernotec.driverschedule.service.scheduling.command.schedule.ScheduleTransportationUpdateCmd;
import com.kernotec.driverschedule.service.scheduling.jpa.dto.ScheduleTransportationDto;
import com.kernotec.driverschedule.service.scheduling.jpa.dto.TripAssignmentDto;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverschedule.service.scheduling.jpa.service.ScheduleTransportationStateService;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TripAssignmentService;
import com.kernotec.driverschedule.service.scheduling.notification.DriverAssignmentPushNotification;
import com.kernotec.driverschedule.service.scheduling.notification.SchedulePushNotification;
import com.kernotec.driverschedule.service.scheduling.rest.dto.request.schedule.ScheduleTransportationUpdateRequest;
import com.kernotec.driverschedule.service.scheduling.rest.dto.request.schedule.TripAssignmentCreateRequest;
import com.kernotec.driverschedule.service.scheduling.socket.ScheduleSocketTopic;
import com.kernotec.driverschedule.service.scheduling.socket.ScheduleTransportationSocketHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
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
public class ProcessScheduleTransportationUpdateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessScheduleTransportationUpdateRequestCmd.Request, Void>
{

    private final ScheduleTransportationStateService scheduleTransportationStateService;
    private final TripAssignmentService tripAssignmentService;

    private final ScheduleTransportationDateValidationCmd scheduleTransportationDateValidationCmd;
    private final ScheduleTransportationUpdateCmd scheduleTransportationUpdateCmd;
    private final RescheduleReasonCreateCmd rescheduleReasonCreateCmd;
    private final ScheduleTransportationGetDtoCmd scheduleTransportationGetDtoCmd;
    private final ScheduleTransportationLogCreateCmd scheduleTransportationLogCreateCmd;

    private final com.kernotec.driverschedule.common.datetime.ZonedDateTimeService zonedDateTimeUtil;
    private final ScheduleTransportationUtil scheduleTransportationUtil;

    private final ScheduleTransportationSocketHandler scheduleTransportationSocketHandler;
    private final SchedulePushNotification schedulePushNotification;
    private final DriverAssignmentPushNotification driverAssignmentPushNotification;

    @Override
    protected void validate(Request request) {
        ScheduleTransportationUpdateRequest scheduleTransportationUpdateRequest = request.scheduleTransportationUpdateRequest;

        ScheduleTransportationDto scheduleTransportationDto = scheduleTransportationGetDtoCmd.withRequest(
                ScheduleTransportationGetDtoCmd.Request.builder()
                    .scheduleTransportationId(request.scheduleTransportationId)
                    .build())
            .execute();

        scheduleTransportationUtil.validateTransitionOfDto(
            scheduleTransportationDto, ScheduleTransportationStateEnum.RESCHEDULED);

        Set<UUID> vehicleIds = scheduleTransportationUtil.getValuesOfTripAssignmentRequest(
            scheduleTransportationUpdateRequest.getTripAssignments(),
            TripAssignmentCreateRequest::getVehicleId
        );

        Set<UUID> driverIds = scheduleTransportationUtil.getValuesOfTripAssignmentRequest(
            scheduleTransportationUpdateRequest.getTripAssignments(),
            TripAssignmentCreateRequest::getDriverId
        );

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

        ScheduleTransportationDto scheduleTransportationDto = scheduleTransportationGetDtoCmd.withRequest(
                ScheduleTransportationGetDtoCmd.Request.builder()
                    .scheduleTransportationId(request.scheduleTransportationId())
                    .build())
            .execute();

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

        handleNotification(
            request.scheduleTransportationId(), scheduleTransportationDto,
            scheduleTransportationUpdateRequest
        );

        handleSocket(scheduleTransportationDto);

        return null;
    }

    private void handleNotification(UUID scheduleTransportationId,
        ScheduleTransportationDto scheduleTransportationDto,
        ScheduleTransportationUpdateRequest scheduleUpdateRequest)
    {
        schedulePushNotification.onReschedule(
            scheduleTransportationId, Set.of(scheduleTransportationDto.getPersonRequestedId()));

        Set<UUID> driverRegisterIds = scheduleTransportationDto.getTripAssignments()
            .stream()
            .map(TripAssignmentDto::getDriverId)
            .collect(Collectors.toSet());

        Set<UUID> driverRequestIds = scheduleTransportationUtil.getValuesOfTripAssignmentRequest(
            scheduleUpdateRequest.getTripAssignments(), TripAssignmentCreateRequest::getDriverId);

        Set<UUID> driverIds = driverRequestIds.stream()
            .filter(driverId -> !driverRegisterIds.contains(driverId))
            .collect(Collectors.toSet());

        if (driverIds.isEmpty()) {
            return;
        }

        driverAssignmentPushNotification.onAssignmentTo(scheduleTransportationId, driverIds);
    }

    private void handleSocket(ScheduleTransportationDto scheduleTransportationDto) {
        UUID userToEmit = scheduleTransportationDto.getPersonRequested()
            .getUserId();

        scheduleTransportationSocketHandler.emitMessage(
            ScheduleTransportationSocketHandler.Request.builder()
                .scheduleTransportationId(scheduleTransportationDto.getId())
                .topic(ScheduleSocketTopic.SCHEDULE_TRANSPORTATION_RESCHEDULED)
                .build());

        scheduleTransportationSocketHandler.emitMessage(
            ScheduleTransportationSocketHandler.Request.builder()
                .scheduleTransportationId(scheduleTransportationDto.getId())
                .topic(ScheduleSocketTopic.SCHEDULE_TRANSPORTATION_RESCHEDULED_TO_USER)
                .toList(Set.of(userToEmit))
                .build());
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId,
                          @NotNull @Valid ScheduleTransportationUpdateRequest scheduleTransportationUpdateRequest)
    {

    }
}
