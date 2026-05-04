package com.kernotec.driverscheduleservice.rest.command.trip.trip.emergency;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.trip.emergency.response.EmergencyResponseCreateCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.TripGetDtoCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.TripUpdateCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.emergency.TripEmergencyGetDtoCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.emergency.TripEmergencyUpdateCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.log.TripLogCreateCmd;
import com.kernotec.driverscheduleservice.exception.trip.TripEmergencyException;
import com.kernotec.driverscheduleservice.exception.trip.TripException;
import com.kernotec.driverscheduleservice.jpa.dto.trip.TripDto;
import com.kernotec.driverscheduleservice.jpa.dto.trip.TripEmergencyDto;
import com.kernotec.driverscheduleservice.jpa.entity.trip.EmergencyResponseType;
import com.kernotec.driverscheduleservice.jpa.enums.trip.EmergencyResponseTypeCodeEnum;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripEmergencyStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.trip.EmergencyResponseTypeService;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripEmergencyStateService;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripStateService;
import com.kernotec.driverscheduleservice.notification.dto.NotificationSendRequest;
import com.kernotec.driverscheduleservice.notification.service.NotificationOrchestrator;
import com.kernotec.driverscheduleservice.notification.templates.NotificationTemplate.TripEmergencyHandledTemplate;
import com.kernotec.driverscheduleservice.rest.command.trip.trip.TripVerifyAndUpdateScheduleCmd;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.emergency.TripEmergencyHandledRequest;
import com.kernotec.driverscheduleservice.rest.socket.trip.TripEmergencySocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    private final TripEmergencySocketHandler tripEmergencySocketHandler;
    private final TripVerifyAndUpdateScheduleCmd tripVerifyAndUpdateScheduleCmd;
    private final TripGetDtoCmd tripGetDtoCmd;
    private final NotificationOrchestrator notificationOrchestrator;

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

        UUID tripEmergencyStateHandledId = tripEmergencyStateService.findIdByCodeThrow(
            TripEmergencyStateEnum.HANDLED);

        UUID tripStateFinalizedId = tripStateService.findIdByCodeThrow(TripStateEnum.FINALIZED);

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

        tripUpdateCmd.withRequest(TripUpdateCmd.Request.builder()
                .tripId(tripEmergencyDto.getTripId())
                .tripStateId(tripStateFinalizedId)
                .build())
            .execute();

        tripLogCreateCmd.withRequest(TripLogCreateCmd.Request.builder()
                .tripId(tripEmergencyDto.getTripId())
                .tripStateId(tripStateFinalizedId)
                .build())
            .execute();

        if (!emergencyResponseTypeCode.equals(
            EmergencyResponseTypeCodeEnum.SEND_REPLACEMENT_UNIT))
        {
            updateSchedule(tripEmergencyDto.getTripId());
        }

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(TripEmergencyHandledTemplate.TITLE)
            .body(TripEmergencyHandledTemplate.BODY)
            .campaignRecipient(TripEmergencyHandledTemplate.RECEIVER)
            .dataMap(Map.of("screen", "trip-emergency/" + request.tripEmergencyId()))
            .personIds(Set.of(tripEmergencyDto.getPersonEmergencyReportedId()))
            .build());

        tripEmergencySocketHandler.emitMessage(TripEmergencySocketHandler.Request.builder()
            .tripEmergencyId(request.tripEmergencyId())
            .topic(WebSocketTopic.TRIP_EMERGENCY_HANDLED_TO_USER)
            .toList(Set.of(tripEmergencyDto.getPersonEmergencyReported()
                .getUserId()))
            .build());

        return null;
    }

    private void validateEmergencyHandled(TripEmergencyDto tripEmergencyDto) {
        TripEmergencyStateEnum emergencyCurrentState = TripEmergencyStateEnum.fromValue(
            tripEmergencyDto.getTripEmergencyState()
                .getCode());

        if (!emergencyCurrentState.canTransitionTo(TripEmergencyStateEnum.HANDLED)) {
            throw new TripEmergencyException(
                "transition.not.allowed", "'" + TripEmergencyStateEnum.HANDLED + "'",
                HttpStatus.CONFLICT.value()
            );
        }

        TripStateEnum tripCurrentState = TripStateEnum.fromValue(tripEmergencyDto.getTrip()
            .getTripState()
            .getCode());

        if (!tripCurrentState.equals(TripStateEnum.EMERGENCY)) {
            throw new TripException(
                "action.not.available", "'" + TripEmergencyStateEnum.HANDLED + "'",
                HttpStatus.CONFLICT.value()
            );
        }
    }

    private void updateSchedule(UUID tripId) {
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