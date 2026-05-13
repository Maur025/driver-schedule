package com.kernotec.driverschedule.service.trip.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.schedule.rest.command.ProcessScheduleAddAssignmentRequestCmd;
import com.kernotec.driverschedule.service.schedule.rest.dto.request.ScheduleAddAssignmentRequest;
import com.kernotec.driverschedule.service.trip.command.EmergencyResponseCreateCmd;
import com.kernotec.driverschedule.service.trip.command.TripEmergencyGetDtoCmd;
import com.kernotec.driverschedule.service.trip.command.TripEmergencyUpdateCmd;
import com.kernotec.driverschedule.service.trip.command.TripGetDtoCmd;
import com.kernotec.driverschedule.service.trip.command.TripLogCreateCmd;
import com.kernotec.driverschedule.service.trip.command.TripUpdateCmd;
import com.kernotec.driverschedule.service.trip.exception.TripEmergencyException;
import com.kernotec.driverschedule.service.trip.exception.TripException;
import com.kernotec.driverschedule.service.trip.jpa.dto.TripDto;
import com.kernotec.driverschedule.service.trip.jpa.dto.TripEmergencyDto;
import com.kernotec.driverschedule.service.trip.jpa.entity.EmergencyResponseType;
import com.kernotec.driverschedule.service.trip.jpa.enums.EmergencyResponseTypeCodeEnum;
import com.kernotec.driverschedule.service.trip.jpa.enums.TripEmergencyStateEnum;
import com.kernotec.driverschedule.service.trip.jpa.enums.TripStateEnum;
import com.kernotec.driverschedule.service.trip.jpa.service.EmergencyResponseTypeService;
import com.kernotec.driverschedule.service.trip.jpa.service.TripEmergencyStateService;
import com.kernotec.driverschedule.service.trip.jpa.service.TripStateService;
import com.kernotec.driverschedule.service.trip.notification.TripEmergencyPushNotification;
import com.kernotec.driverschedule.service.trip.rest.dto.request.TripEmergencyHandledRequest;
import com.kernotec.driverschedule.service.trip.socket.TripEmergencySocketHandler;
import com.kernotec.driverschedule.service.trip.socket.TripSocketHandler;
import com.kernotec.driverschedule.service.trip.socket.TripSocketTopic;
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
public class TripEmergencyHandledCmd extends
    AbstractTransactionalRequiredCommand<TripEmergencyHandledCmd.Request, Void>
{

    private final EmergencyResponseTypeService emergencyResponseTypeService;
    private final TripEmergencyStateService tripEmergencyStateService;
    private final TripStateService tripStateService;

    private final EmergencyResponseCreateCmd emergencyResponseCreateCmd;
    private final TripEmergencyUpdateCmd tripEmergencyUpdateCmd;
    private final TripUpdateCmd tripUpdateCmd;
    private final TripEmergencyGetDtoCmd tripEmergencyGetDtoCmd;
    private final TripLogCreateCmd tripLogCreateCmd;
    private final TripVerifyAndUpdateScheduleCmd tripVerifyAndUpdateScheduleCmd;
    private final TripGetDtoCmd tripGetDtoCmd;
    private final ProcessScheduleAddAssignmentRequestCmd processScheduleAddAssignmentRequestCmd;

    private final TripEmergencySocketHandler tripEmergencySocketHandler;
    private final TripEmergencyPushNotification tripEmergencyPushNotification;
    private final TripSocketHandler tripSocketHandler;

    @Override
    protected Void run(Request request) {
        TripEmergencyHandledRequest tripEmergencyHandledRequest = request.tripEmergencyHandledRequest();

        TripEmergencyDto tripEmergencyDto = tripEmergencyGetDtoCmd.withRequest(
                TripEmergencyGetDtoCmd.Request.builder()
                    .tripEmergencyId(request.tripEmergencyId())
                    .build())
            .execute();

        validateEmergencyHandled(tripEmergencyDto);

        EmergencyResponseType emergencyResponseType = emergencyResponseTypeService.findByIdThrow(
            tripEmergencyHandledRequest.getEmergencyResponseTypeId());

        EmergencyResponseTypeCodeEnum emergencyResponseTypeCode = EmergencyResponseTypeCodeEnum.fromValue(
            emergencyResponseType.getCode());

        assignmentDriversInSchedule(
            emergencyResponseTypeCode, tripEmergencyHandledRequest.getAddAssignment(),
            tripEmergencyDto
        );

        handledEmergencyResponse(request);

        updateTripState(tripEmergencyDto);

        updateSchedule(tripEmergencyDto.getTripId(), emergencyResponseTypeCode);

        tripEmergencyPushNotification.onHandled(
            request.tripEmergencyId(), Set.of(tripEmergencyDto.getPersonEmergencyReportedId()));

        tripEmergencySocketHandler.emitMessage(TripEmergencySocketHandler.Request.builder()
            .tripEmergencyId(request.tripEmergencyId())
            .topic(TripSocketTopic.TRIP_EMERGENCY_HANDLED_TO_USER)
            .toList(Set.of(tripEmergencyDto.getPersonEmergencyReported()
                .getUserId()))
            .build());

        return null;
    }

    private void validateEmergencyHandled(TripEmergencyDto tripEmergencyDto)
    {
        TripEmergencyStateEnum emergencyCurrentState = TripEmergencyStateEnum.fromValue(
            tripEmergencyDto.getTripEmergencyState()
                .getCode());

        if (!emergencyCurrentState.canTransitionTo(TripEmergencyStateEnum.HANDLED)) {
            throw new TripEmergencyException(
                "transition.not.allowed",
                "'" + TripEmergencyStateEnum.HANDLED + "'", HttpStatus.CONFLICT.value()
            );
        }

        TripStateEnum tripCurrentState = TripStateEnum.fromValue(tripEmergencyDto.getTrip()
            .getTripState()
            .getCode());

        if (!tripCurrentState.equals(TripStateEnum.EMERGENCY)) {
            throw new TripException(
                "action.not.available",
                "'" + TripEmergencyStateEnum.HANDLED + "'", HttpStatus.CONFLICT.value()
            );
        }
    }

    private void handledEmergencyResponse(Request request) {
        TripEmergencyHandledRequest tripEmergencyHandledRequest = request.tripEmergencyHandledRequest();

        UUID tripEmergencyStateHandledId = tripEmergencyStateService.findIdByCodeThrow(
            TripEmergencyStateEnum.HANDLED);

        emergencyResponseCreateCmd.withRequest(EmergencyResponseCreateCmd.Request.builder()
                .tripEmergencyId(request.tripEmergencyId())
                .detail(tripEmergencyHandledRequest.getDetail())
                .emergencyResponseTypeId(tripEmergencyHandledRequest.getEmergencyResponseTypeId())
                .build())
            .execute();

        tripEmergencyUpdateCmd.withRequest(TripEmergencyUpdateCmd.Request.builder()
                .tripEmergencyId(request.tripEmergencyId())
                .tripEmergencyStateId(tripEmergencyStateHandledId)
                .build())
            .execute();
    }

    private void updateTripState(TripEmergencyDto tripEmergencyDto) {
        UUID tripStateFinalizedId = tripStateService.findIdByCodeThrow(TripStateEnum.FINALIZED);

        tripUpdateCmd.withRequest(TripUpdateCmd.Request.builder()
                .tripId(tripEmergencyDto.getTripId())
                .tripStateId(tripStateFinalizedId)
                .tripEnd(ZonedDateTime.now())
                .build())
            .execute();

        tripLogCreateCmd.withRequest(TripLogCreateCmd.Request.builder()
                .tripId(tripEmergencyDto.getTripId())
                .tripStateId(tripStateFinalizedId)
                .build())
            .execute();

        tripSocketHandler.emitMessage(TripSocketHandler.Request.builder()
            .tripId(tripEmergencyDto.getTripId())
            .topic(TripSocketTopic.TRIP_FINALIZED_TO_USER)
            .toList(Set.of(tripEmergencyDto.getPersonEmergencyReported()
                .getUserId()))
            .build());
    }

    private void assignmentDriversInSchedule(
        EmergencyResponseTypeCodeEnum emergencyResponseTypeCode,
        ScheduleAddAssignmentRequest scheduleAddAssignmentRequest,
        TripEmergencyDto tripEmergencyDto)
    {
        if (!emergencyResponseTypeCode.equals(
            EmergencyResponseTypeCodeEnum.SEND_REPLACEMENT_UNIT))
        {
            log.debug("This action not available to states distinct to SEND_REPLACEMENT_UNIT");
            return;
        }

        if (scheduleAddAssignmentRequest == null) {
            throw new TripEmergencyException(
                "add.assignment.not.null", "", HttpStatus.BAD_REQUEST.value());
        }

        processScheduleAddAssignmentRequestCmd.withRequest(
                ProcessScheduleAddAssignmentRequestCmd.Request.builder()
                    .scheduleTransportationId(tripEmergencyDto.getScheduleTransportationId())
                    .scheduleAddAssignmentRequest(scheduleAddAssignmentRequest)
                    .build())
            .execute();
    }

    private void updateSchedule(UUID tripId,
        EmergencyResponseTypeCodeEnum emergencyResponseTypeCode)
    {
        if (emergencyResponseTypeCode.equals(EmergencyResponseTypeCodeEnum.SEND_REPLACEMENT_UNIT)) {
            log.debug("Action selected not trigger a update schedule");
            return;
        }

        TripDto tripDto = tripGetDtoCmd.withRequest(TripGetDtoCmd.Request.builder()
                .tripId(tripId)
                .build())
            .execute();

        tripVerifyAndUpdateScheduleCmd.withRequest(TripVerifyAndUpdateScheduleCmd.Request.builder()
                .tripDto(tripDto)
                .build())
            .execute();
    }

    @Builder
    public record Request(@NotNull UUID tripEmergencyId,
                          @NotNull TripEmergencyHandledRequest tripEmergencyHandledRequest)
    {

    }
}