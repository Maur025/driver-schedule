package com.kernotec.driverscheduleservice.rest.command.schedule.transportation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.reason.ReasonCreateCmd;
import com.kernotec.driverscheduleservice.command.reschedule.reason.RescheduleReasonCreateCmd;
import com.kernotec.driverscheduleservice.command.schedule.transportation.ScheduleTransportationGetDtoCmd;
import com.kernotec.driverscheduleservice.command.schedule.transportation.ScheduleTransportationUpdateCmd;
import com.kernotec.driverscheduleservice.exception.ScheduleTransportationException;
import com.kernotec.driverscheduleservice.jpa.dto.ScheduleTransportationDto;
import com.kernotec.driverscheduleservice.jpa.dto.ScheduleTransportationStateDto;
import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.ScheduleTransportationService;
import com.kernotec.driverscheduleservice.jpa.service.ScheduleTransportationStateService;
import com.kernotec.driverscheduleservice.rest.dto.request.schedule.transportation.ScheduleTransportationUpdateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.transportation.ScheduleTransportationResponseMapper;
import com.kernotec.driverscheduleservice.util.ZonedDateTimeUtil;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
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
public class ProcessScheduleTransportationUpdateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessScheduleTransportationUpdateRequestCmd.Request, Void>
{

    private final ScheduleTransportationStateService scheduleTransportationStateService;
    private final ScheduleTransportationService scheduleTransportationService;

    private final ScheduleTransportationResponseMapper scheduleTransportationResponseMapper;

    private final ScheduleTransportationDateValidationCmd scheduleTransportationDateValidationCmd;
    private final ScheduleTransportationUpdateCmd scheduleTransportationUpdateCmd;
    private final WebSocketHandler webSocketHandler;
    private final ZonedDateTimeUtil zonedDateTimeUtil;
    private final ReasonCreateCmd reasonCreateCmd;
    private final RescheduleReasonCreateCmd rescheduleReasonCreateCmd;
    private final ScheduleTransportationGetDtoCmd scheduleTransportationGetDtoCmd;

    @Override
    protected void validate(Request request) {
        ScheduleTransportationUpdateRequest scheduleTransportationUpdateRequest = request.scheduleTransportationUpdateRequest;

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
                "is.cancelled", "'" + request.scheduleTransportationId + "'",
                HttpStatus.CONFLICT.value()
            );
        }

        scheduleTransportationDateValidationCmd.withRequest(
                ScheduleTransportationDateValidationCmd.Request.builder()
                    .vehicleId(scheduleTransportationUpdateRequest.getVehicleId())
                    .driverId(scheduleTransportationUpdateRequest.getDriverId())
                    .requestedDate(scheduleTransportationUpdateRequest.getRequestedDate())
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

        ZonedDateTime scheduledFrom = zonedDateTimeUtil.getNewOfDateAndTime(
            scheduleTransportationUpdateRequest.getRequestedDate(),
            scheduleTransportationUpdateRequest.getRequestedStartTime()
        );

        ZonedDateTime scheduledTo = zonedDateTimeUtil.getNewOfDateAndTime(
            scheduleTransportationUpdateRequest.getRequestedDate(),
            scheduleTransportationUpdateRequest.getRequestedEndTime()
        );

        scheduleTransportationUpdateCmd.withRequest(
                ScheduleTransportationUpdateCmd.Request.builder()
                    .scheduleTransportationId(request.scheduleTransportationId)
                    .scheduleFrom(scheduledFrom)
                    .scheduleTo(scheduledTo)
                    .vehicleId(scheduleTransportationUpdateRequest.getVehicleId())
                    .driverId(scheduleTransportationUpdateRequest.getDriverId())
                    .scheduleTransportationStateId(scheduleTransportationStateRescheduledId)
                    .build())
            .execute();

        registerRescheduleReason(
            request.scheduleTransportationId,
            scheduleTransportationUpdateRequest.getRescheduleReason()
        );

        ScheduleTransportation scheduleTransportation = scheduleTransportationService.findByIdThrow(
            request.scheduleTransportationId);

        webSocketHandler.emitMessage(
            WebSocketTopic.SCHEDULE_TRANSPORTATION_RESCHEDULED,
            WebSocketSingleResponse.<ScheduleTransportationResponse>builder()
                .topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_RESCHEDULED)
                .timestamp(ZonedDateTime.now())
                .data(scheduleTransportationResponseMapper.toResponse(scheduleTransportation))
                .build()
        );

        return null;
    }

    private void registerRescheduleReason(UUID scheduleTransportationId, String rescheduleReason) {
        if (rescheduleReason == null || rescheduleReason.isBlank()
            || scheduleTransportationId == null)
        {
            log.debug(
                "No reschedule reason provided or scheduleTransportationId is null, skipping registration.");
            return;
        }

        UUID reasonId = reasonCreateCmd.withRequest(ReasonCreateCmd.Request.builder()
                .reasonDescription(rescheduleReason)
                .build())
            .execute();

        rescheduleReasonCreateCmd.withRequest(RescheduleReasonCreateCmd.Request.builder()
                .reasonId(reasonId)
                .scheduleTransportationId(scheduleTransportationId)
                .build())
            .execute();
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId,
                          @NotNull ScheduleTransportationUpdateRequest scheduleTransportationUpdateRequest)
    {

    }
}
