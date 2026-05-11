package com.kernotec.driverschedule.service.rest.command.trip.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
import com.kernotec.driverschedule.service.command.schedule.schedule.transportation.ScheduleTransportationUpdateCmd;
import com.kernotec.driverschedule.service.command.schedule.trip.assignment.TripAssignmentGetDtoCmd;
import com.kernotec.driverschedule.service.command.trip.trip.TripCreateCmd;
import com.kernotec.driverschedule.service.command.trip.trip.log.TripLogCreateCmd;
import com.kernotec.driverschedule.service.common.dto.Coordinate;
import com.kernotec.driverschedule.service.exception.schedule.ScheduleTransportationException;
import com.kernotec.driverschedule.service.exception.trip.TripException;
import com.kernotec.driverschedule.service.jpa.dto.schedule.ScheduleTransportationDto;
import com.kernotec.driverschedule.service.jpa.dto.schedule.TripAssignmentDto;
import com.kernotec.driverschedule.service.jpa.entity.trip.Trip;
import com.kernotec.driverschedule.service.jpa.enums.schedule.ScheduleTransportationStateEnum;
import com.kernotec.driverschedule.service.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverschedule.service.jpa.service.resource.LocationService;
import com.kernotec.driverschedule.service.jpa.service.schedule.ScheduleTransportationStateService;
import com.kernotec.driverschedule.service.jpa.service.trip.TripService;
import com.kernotec.driverschedule.service.jpa.service.trip.TripStateService;
import com.kernotec.driverschedule.service.rest.dto.trip.request.trip.TripCreateRequest;
import com.kernotec.driverschedule.service.rest.dto.trip.request.trip.TripFilterRequest;
import com.kernotec.driverschedule.service.rest.socket.schedule.ScheduleTransportationSocketHandler;
import com.kernotec.driverschedule.service.rest.socket.trip.TripSocketHandler;
import com.kernotec.driverschedule.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessTripCreateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessTripCreateRequestCmd.Request, UUID>
{

    private final TripStateService tripStateService;
    private final ScheduleTransportationStateService scheduleTransportationStateService;
    private final LocationService locationService;
    private final TripService tripService;
    private final PersonService personService;

    private final TripAssignmentGetDtoCmd tripAssignmentGetDtoCmd;
    private final ScheduleTransportationUpdateCmd scheduleTransportationUpdateCmd;
    private final TripCreateCmd tripCreateCmd;
    private final TripLogCreateCmd tripLogCreateCmd;
    private final ScheduleTransportationSocketHandler scheduleTransportationSocketHandler;
    private final TripSocketHandler tripSocketHandler;

    @Override
    protected void validate(Request request) {
        Pageable pageable = PageableUtil.of(0, 10, "createdAt", false);
        UUID driverId = personService.findIdByUserIdAuthenticateThrow();

        TripFilterRequest filterRequest = new TripFilterRequest();
        filterRequest.setDriverId(driverId);
        filterRequest.setDeleted(false);
        filterRequest.setTripStates(
            Set.of(TripStateEnum.ON_ROUTE, TripStateEnum.WAITING, TripStateEnum.EMERGENCY));

        Page<Trip> tripPage = tripService.findAllBySearch(filterRequest, pageable);

        if (tripPage.getTotalElements() > 0) {
            throw new TripException(
                "driver.already.trip.in.progress", "", HttpStatus.CONFLICT.value());
        }
    }

    @Override
    protected UUID run(Request request) {
        TripCreateRequest tripCreateRequest = request.tripCreateRequest();

        TripAssignmentDto tripAssignmentDto = tripAssignmentGetDtoCmd.withRequest(
                TripAssignmentGetDtoCmd.Request.builder()
                    .tripAssignmentId(tripCreateRequest.getTripAssignmentId())
                    .build())
            .execute();

        validateBelongingDriver(tripAssignmentDto.getDriverId());

        validateDateToStart(
            tripAssignmentDto.getEstimatedStartTime(), tripCreateRequest.getZoneId());

        updateScheduleStateToInProgress(
            tripAssignmentDto.getScheduleTransportation(),
            tripAssignmentDto.getScheduleTransportationId()
        );

        UUID tripStateOnRouteId = tripStateService.findIdByCodeThrow(TripStateEnum.ON_ROUTE);

        UUID tripId = tripCreateCmd.withRequest(TripCreateCmd.Request.builder()
                .tripStateId(tripStateOnRouteId)
                .tripAssignmentId(tripCreateRequest.getTripAssignmentId())
                .durationTotalMinutes(0d)
                .onRouteTimeMinutes(0d)
                .waitTimeMinutes(0d)
                .tripStart(ZonedDateTime.now())
                .build())
            .execute();

        Coordinate coordinate = locationService.getCoordinateOfList(
            Arrays.asList(tripCreateRequest.getLongitude(), tripCreateRequest.getLatitude()));

        tripLogCreateCmd.withRequest(TripLogCreateCmd.Request.builder()
                .tripId(tripId)
                .tripStateId(tripStateOnRouteId)
                .coordinate(coordinate)
                .build())
            .execute();

        tripSocketHandler.emitMessage(TripSocketHandler.Request.builder()
            .tripId(tripId)
            .topic(WebSocketTopic.TRIP_STARTED)
            .build());

        return tripId;
    }

    private void validateBelongingDriver(UUID registerDriverId) {
        UUID driverId = personService.findIdByUserIdAuthenticateThrow();

        if (!registerDriverId.equals(driverId)) {
            throw new TripException("driver.assignment.conflict", "", HttpStatus.CONFLICT.value());
        }
    }

    private void validateDateToStart(ZonedDateTime startDateTime, String zoneId) {
        if (!com.kernotec.driverschedule.common.datetime.ZonedDateTimeService.isSameDay(startDateTime, zoneId)) {
            throw new TripException(
                "should.start.not.on.scheduled.day", "", HttpStatus.CONFLICT.value());
        }
    }

    private void updateScheduleStateToInProgress(
        ScheduleTransportationDto scheduleTransportationDto, UUID scheduleTransportationId)
    {
        ScheduleTransportationStateEnum scheduleCurrentState = ScheduleTransportationStateEnum.fromValue(
            scheduleTransportationDto.getScheduleTransportationState()
                .getCode());

        validateScheduleStateTransition(scheduleCurrentState);

        if (ScheduleTransportationStateEnum.IN_PROGRESS.equals(scheduleCurrentState)) {
            return;
        }

        UUID scheduleStateInProgressId = scheduleTransportationStateService.findIdByCodeThrow(
            ScheduleTransportationStateEnum.IN_PROGRESS);

        scheduleTransportationUpdateCmd.withRequest(
                ScheduleTransportationUpdateCmd.Request.builder()
                    .scheduleTransportationId(scheduleTransportationId)
                    .scheduleTransportationStateId(scheduleStateInProgressId)
                    .build())
            .execute();

        scheduleTransportationSocketHandler.emitMessage(
            ScheduleTransportationSocketHandler.Request.builder()
                .scheduleTransportationId(scheduleTransportationId)
                .topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_ON_PROGRESS)
                .build());
    }

    private void validateScheduleStateTransition(
        ScheduleTransportationStateEnum scheduleCurrentState)
    {
        if (!scheduleCurrentState.canTransitionTo(ScheduleTransportationStateEnum.IN_PROGRESS)) {
            throw new ScheduleTransportationException(
                "not.supported.state",
                "'" + scheduleCurrentState + "'", HttpStatus.CONFLICT.value()
            );
        }
    }

    @Builder
    public record Request(@NotNull TripCreateRequest tripCreateRequest) {

    }
}
