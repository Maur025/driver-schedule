package com.kernotec.driverscheduleservice.rest.command.schedule.schedule.transportation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.request.transportation.request.TransportationRequestGetDtoCmd;
import com.kernotec.driverscheduleservice.command.request.transportation.request.TransportationRequestUpdateCmd;
import com.kernotec.driverscheduleservice.command.request.transportation.request.log.TransportationRequestLogCreateCmd;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.ScheduleTransportationCreateCmd;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.log.ScheduleTransportationLogCreateCmd;
import com.kernotec.driverscheduleservice.exception.schedule.ScheduleTransportationException;
import com.kernotec.driverscheduleservice.jpa.dto.request.TransportationRequestDto;
import com.kernotec.driverscheduleservice.jpa.enums.request.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.schedule.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.request.TransportationRequestStateService;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationStateService;
import com.kernotec.driverscheduleservice.rest.dto.schedule.request.schedule.transportation.ScheduleTransportationCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.schedule.request.trip.assignment.TripAssignmentCreateRequest;
import com.kernotec.driverscheduleservice.rest.socket.schedule.ScheduleTransportationSocketHandler;
import com.kernotec.driverscheduleservice.util.ScheduleTransportationUtil;
import com.kernotec.driverscheduleservice.util.ScheduleTransportationUtil.RegistryTripAssignmentRequest;
import com.kernotec.driverscheduleservice.util.ZonedDateTimeUtil;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
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

    private final ZonedDateTimeUtil zonedDateTimeUtil;
    private final ScheduleTransportationUtil scheduleTransportationUtil;
    private final ScheduleTransportationSocketHandler scheduleTransportationSocketHadler;

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
                "invalid.state.to.action", "'" + requestStateCurrent + "'",
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

        socketHandler(scheduleTransportationId, transportationRequestDto);

        return scheduleTransportationId;
    }


    private void socketHandler(UUID scheduleTransportationId,
        TransportationRequestDto transportationRequestDto)
    {
        UUID userToEmit = transportationRequestDto.getPersonRequested()
            .getUserId();

        scheduleTransportationSocketHadler.emitMessage(
            ScheduleTransportationSocketHandler.Request.builder()
                .scheduleTransportationId(scheduleTransportationId)
                .topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_CREATED)
                .build());

        scheduleTransportationSocketHadler.emitMessage(
            ScheduleTransportationSocketHandler.Request.builder()
                .scheduleTransportationId(scheduleTransportationId)
                .topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_CREATED_TO_USER)
                .toList(Set.of(userToEmit))
                .build());
    }

    @Builder
    public record Request(
        @NotNull ScheduleTransportationCreateRequest scheduleTransportationCreateRequest)
    {

    }
}
