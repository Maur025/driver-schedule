package com.kernotec.driverschedule.service.schedule.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.common.util.ScheduleTransportationUtil;
import com.kernotec.driverschedule.service.common.util.ScheduleTransportationUtil.RegistryTripAssignmentRequest;
import com.kernotec.driverschedule.service.schedule.command.ScheduleTransportationGetDtoCmd;
import com.kernotec.driverschedule.service.schedule.exception.ScheduleTransportationException;
import com.kernotec.driverschedule.service.schedule.jpa.dto.ScheduleTransportationDto;
import com.kernotec.driverschedule.service.schedule.jpa.dto.TripAssignmentDto;
import com.kernotec.driverschedule.service.schedule.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverschedule.service.schedule.notification.DriverAssignmentPushNotification;
import com.kernotec.driverschedule.service.schedule.rest.dto.request.ScheduleAddAssignmentRequest;
import com.kernotec.driverschedule.service.schedule.rest.dto.request.TripAssignmentCreateRequest;
import com.kernotec.driverschedule.service.schedule.socket.ScheduleTransportationSocketHandler;
import com.kernotec.driverschedule.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessScheduleAddAssignmentRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessScheduleAddAssignmentRequestCmd.Request, Void>
{

    private final ScheduleTransportationGetDtoCmd scheduleTransportationGetDtoCmd;
    private final ScheduleTransportationDateValidationCmd scheduleTransportationDateValidationCmd;
    private final ScheduleTransportationUtil scheduleTransportationUtil;
    private final ScheduleTransportationSocketHandler scheduleTransportationSocketHadler;
    private final DriverAssignmentPushNotification driverAssignmentPushNotification;

    @Override
    protected void validate(Request request) {
        ScheduleAddAssignmentRequest scheduleAddAssignmentRequest = request.scheduleAddAssignmentRequest;

        ScheduleTransportationDto scheduleTransportationDto = scheduleTransportationGetDtoCmd.withRequest(
                ScheduleTransportationGetDtoCmd.Request.builder()
                    .scheduleTransportationId(request.scheduleTransportationId)
                    .build())
            .execute();

        ScheduleTransportationStateEnum scheduleCurrentState = scheduleTransportationUtil.getCurrentScheduleStateOfDto(
            scheduleTransportationDto);

        if (!scheduleCurrentState.equals(ScheduleTransportationStateEnum.IN_PROGRESS)) {
            throw new ScheduleTransportationException(
                "invalid.state.to.action", "'" + scheduleCurrentState + "'", HttpStatus.OK.value());
        }

        Set<UUID> vehicleIdSet = scheduleTransportationUtil.getValuesOfTripAssignmentRequest(
            scheduleAddAssignmentRequest.getTripAssignments(),
            TripAssignmentCreateRequest::getVehicleId
        );

        Set<UUID> driverIdSet = scheduleTransportationUtil.getValuesOfTripAssignmentRequest(
            scheduleAddAssignmentRequest.getTripAssignments(),
            TripAssignmentCreateRequest::getDriverId
        );

        verifyDuplicates(scheduleTransportationDto.getTripAssignments(), vehicleIdSet, driverIdSet);

        scheduleTransportationDateValidationCmd.withRequest(
                ScheduleTransportationDateValidationCmd.Request.builder()
                    .vehicleIdList(vehicleIdSet)
                    .driverIdList(driverIdSet)
                    .requestedStartTime(scheduleAddAssignmentRequest.getAssignFrom())
                    .requestedEndTime(scheduleAddAssignmentRequest.getAssignTo())
                    .zoneId(scheduleAddAssignmentRequest.getZoneId())
                    .scheduleTransportationExcludeId(request.scheduleTransportationId)
                    .build())
            .execute();
    }

    private void verifyDuplicates(Collection<TripAssignmentDto> tripAssignmentDtos,
        Set<UUID> vehicleIdSet, Set<UUID> driverIdSet)
    {
        Set<UUID> registeredVehicleIdSet = new HashSet<>();
        Set<UUID> registeredDriverIdSet = new HashSet<>();

        for (TripAssignmentDto assignmentDto : tripAssignmentDtos) {
            registeredVehicleIdSet.add(assignmentDto.getVehicleId());
            registeredDriverIdSet.add(assignmentDto.getDriverId());
        }

        if (driverIdSet.stream()
            .anyMatch(registeredDriverIdSet::contains))
        {
            throw new ScheduleTransportationException("", "", HttpStatus.OK.value());
        }

        if (vehicleIdSet.stream()
            .anyMatch(registeredVehicleIdSet::contains))
        {
            throw new ScheduleTransportationException("", "", HttpStatus.OK.value());
        }
    }

    @Override
    protected Void run(Request request) {
        ScheduleAddAssignmentRequest scheduleAddAssignmentRequest = request.scheduleAddAssignmentRequest;

        scheduleTransportationUtil.registryTripAssignments(RegistryTripAssignmentRequest.builder()
            .tripAssignmentCreateRequestList(scheduleAddAssignmentRequest.getTripAssignments())
            .scheduleTransportationId(request.scheduleTransportationId())
            .estimatedStartTime(scheduleAddAssignmentRequest.getAssignFrom())
            .estimatedEndTime(scheduleAddAssignmentRequest.getAssignTo())
            .build());

        Set<UUID> driverIdSet = scheduleTransportationUtil.getValuesOfTripAssignmentRequest(
            scheduleAddAssignmentRequest.getTripAssignments(),
            TripAssignmentCreateRequest::getDriverId
        );

        driverAssignmentPushNotification.onAssignmentTo(
            request.scheduleTransportationId(), driverIdSet);

        scheduleTransportationSocketHadler.emitMessage(
            ScheduleTransportationSocketHandler.Request.builder()
                .scheduleTransportationId(request.scheduleTransportationId())
                .topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_CREATED)
                .build());

        return null;
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId,
                          @NotNull ScheduleAddAssignmentRequest scheduleAddAssignmentRequest)
    {

    }
}