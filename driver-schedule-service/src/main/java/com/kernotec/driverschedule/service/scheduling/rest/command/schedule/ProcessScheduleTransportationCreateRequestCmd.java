package com.kernotec.driverschedule.service.scheduling.rest.command.schedule;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.common.util.ScheduleTransportationUtil;
import com.kernotec.driverschedule.service.scheduling.common.util.ScheduleTransportationUtil.RegistryTripAssignmentRequest;
import com.kernotec.driverschedule.service.scheduling.command.request.TransportationRequestGetDtoCmd;
import com.kernotec.driverschedule.service.scheduling.command.request.TransportationRequestLogCreateCmd;
import com.kernotec.driverschedule.service.scheduling.command.request.TransportationRequestUpdateCmd;
import com.kernotec.driverschedule.service.scheduling.jpa.dto.TransportationRequestDto;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TransportationRequestStateService;
import com.kernotec.driverschedule.service.scheduling.command.schedule.ScheduleTransportationCreateCmd;
import com.kernotec.driverschedule.service.scheduling.command.schedule.ScheduleTransportationLogCreateCmd;
import com.kernotec.driverschedule.service.scheduling.exception.ScheduleTransportationException;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverschedule.service.scheduling.jpa.service.ScheduleTransportationStateService;
import com.kernotec.driverschedule.service.scheduling.notification.DriverAssignmentPushNotification;
import com.kernotec.driverschedule.service.scheduling.notification.SchedulePushNotification;
import com.kernotec.driverschedule.service.scheduling.rest.dto.request.schedule.ScheduleTransportationCreateRequest;
import com.kernotec.driverschedule.service.scheduling.rest.dto.request.schedule.TripAssignmentCreateRequest;
import com.kernotec.driverschedule.service.scheduling.socket.ScheduleSocketTopic;
import com.kernotec.driverschedule.service.scheduling.socket.ScheduleTransportationSocketHandler;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Set;
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

    private final ScheduleTransportationStateService scheduleTransportationStateService;
    private final TransportationRequestStateService transportationRequestStateService;

    private final TransportationRequestGetDtoCmd transportationRequestGetDtoCmd;
    private final ScheduleTransportationCreateCmd scheduleTransportationCreateCmd;
    private final TransportationRequestUpdateCmd transportationRequestUpdateCmd;
    private final ScheduleTransportationDateValidationCmd scheduleTransportationDateValidationCmd;
    private final ScheduleTransportationLogCreateCmd scheduleTransportationLogCreateCmd;
    private final TransportationRequestLogCreateCmd transportationRequestLogCreateCmd;

    private final com.kernotec.driverschedule.common.datetime.ZonedDateTimeService zonedDateTimeUtil;
    private final ScheduleTransportationUtil scheduleTransportationUtil;
    private final ScheduleTransportationSocketHandler scheduleTransportationSocketHadler;
    private final SchedulePushNotification schedulePushNotification;
    private final DriverAssignmentPushNotification driverAssignmentPushNotification;

    @Override
    protected void validate(Request request) {
        ScheduleTransportationCreateRequest scheduleTransportationCreateRequest = request.scheduleTransportationCreateRequest;

        Set<UUID> vehicleIds = scheduleTransportationUtil.getValuesOfTripAssignmentRequest(
            scheduleTransportationCreateRequest.getTripAssignments(),
            TripAssignmentCreateRequest::getVehicleId
        );

        Set<UUID> driverIds = scheduleTransportationUtil.getValuesOfTripAssignmentRequest(
            scheduleTransportationCreateRequest.getTripAssignments(),
            TripAssignmentCreateRequest::getDriverId
        );

        scheduleTransportationDateValidationCmd.withRequest(
                ScheduleTransportationDateValidationCmd.Request.builder()
                    .vehicleIdList(vehicleIds)
                    .driverIdList(driverIds)
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

        TransportationRequestStateEnum requestStateCurrent = TransportationRequestStateEnum.fromValue(
            transportationRequestDto.getTransportationRequestState()
                .getCode());

        if (!requestStateCurrent.canTransitionTo(TransportationRequestStateEnum.APPROVED)) {
            throw new ScheduleTransportationException(
                "invalid.state.to.action",
                "'" + requestStateCurrent + "'", HttpStatus.BAD_REQUEST.value()
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

        ZonedDateTime scheduledFrom = zonedDateTimeUtil.getDateScheduleNormalized(
            scheduleTransportationCreateRequest.getRequestedStartTime());

        ZonedDateTime scheduledTo = zonedDateTimeUtil.getDateScheduleNormalized(
            scheduleTransportationCreateRequest.getRequestedEndTime());

        UUID scheduleTransportationId = scheduleTransportationCreateCmd.withRequest(
                ScheduleTransportationCreateCmd.Request.builder()
                    .scheduleFrom(scheduledFrom)
                    .scheduleTo(scheduledTo)
                    .scheduledDate(scheduledFrom)
                    .transportationRequestId(
                        scheduleTransportationCreateRequest.getTransportationRequestId())
                    .personRequestedId(transportationRequestDto.getPersonRequestedId())
                    .scheduleTransportationStateId(scheduledTransportationStateScheduledId)
                    .build())
            .execute();

        scheduleTransportationUtil.registryTripAssignments(RegistryTripAssignmentRequest.builder()
            .tripAssignmentCreateRequestList(
                scheduleTransportationCreateRequest.getTripAssignments())
            .scheduleTransportationId(scheduleTransportationId)
            .estimatedStartTime(scheduledFrom)
            .estimatedEndTime(scheduledTo)
            .build());

        scheduleTransportationLogCreateCmd.withRequest(
                ScheduleTransportationLogCreateCmd.Request.builder()
                    .scheduleTransportationId(scheduleTransportationId)
                    .scheduleTransportationStateId(scheduledTransportationStateScheduledId)
                    .build())
            .execute();

        handleNotification(
            scheduleTransportationId, transportationRequestDto,
            scheduleTransportationCreateRequest
        );

        handleSocket(scheduleTransportationId, transportationRequestDto);

        return scheduleTransportationId;
    }


    private void handleNotification(UUID scheduleTransportationId,
        TransportationRequestDto transportationRequestDto,
        ScheduleTransportationCreateRequest scheduleCreateRequest)
    {
        schedulePushNotification.onApproved(
            scheduleTransportationId, Set.of(transportationRequestDto.getPersonRequestedId()));

        Set<UUID> driverIds = scheduleTransportationUtil.getValuesOfTripAssignmentRequest(
            scheduleCreateRequest.getTripAssignments(), TripAssignmentCreateRequest::getDriverId);

        driverAssignmentPushNotification.onAssignmentTo(scheduleTransportationId, driverIds);
    }

    private void handleSocket(UUID scheduleTransportationId,
        TransportationRequestDto transportationRequestDto)
    {
        UUID userToEmit = transportationRequestDto.getPersonRequested()
            .getUserId();

        scheduleTransportationSocketHadler.emitMessage(
            ScheduleTransportationSocketHandler.Request.builder()
                .scheduleTransportationId(scheduleTransportationId)
                .topic(ScheduleSocketTopic.SCHEDULE_TRANSPORTATION_CREATED)
                .build());

        scheduleTransportationSocketHadler.emitMessage(
            ScheduleTransportationSocketHandler.Request.builder()
                .scheduleTransportationId(scheduleTransportationId)
                .topic(ScheduleSocketTopic.SCHEDULE_TRANSPORTATION_CREATED_TO_USER)
                .toList(Set.of(userToEmit))
                .build());
    }

    @Builder
    public record Request(
        @NotNull ScheduleTransportationCreateRequest scheduleTransportationCreateRequest)
    {

    }
}
