package com.kernotec.driverscheduleservice.rest.command.trip.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.ScheduleTransportationUpdateCmd;
import com.kernotec.driverscheduleservice.command.schedule.trip.assignment.TripAssignmentGetDtoCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.TripCreateCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.log.TripLogCreateCmd;
import com.kernotec.driverscheduleservice.exception.schedule.ScheduleTransportationException;
import com.kernotec.driverscheduleservice.exception.trip.TripException;
import com.kernotec.driverscheduleservice.jpa.dto.schedule.TripAssignmentDto;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverscheduleservice.jpa.entity.trip.Trip;
import com.kernotec.driverscheduleservice.jpa.enums.schedule.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.resource.LocationService;
import com.kernotec.driverscheduleservice.jpa.service.resource.PersonService;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationService;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationStateService;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripService;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripStateService;
import com.kernotec.driverscheduleservice.jpa.util.Coordinate;
import com.kernotec.driverscheduleservice.rest.dto.common.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.TripCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.TripFilterRequest;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.TripResponse;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationResponseFlatMapper;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.TripResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
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
    private final WebSocketHandler webSocketHandler;
    private final TripResponseMapper tripResponseMapper;
    private final ScheduleTransportationService scheduleTransportationService;
    private final ScheduleTransportationResponseFlatMapper scheduleTransportationResponseFlatMapper;

    @Override
    protected void validate(Request request) {
        Pageable pageable = PageableUtil.of(0, 10, "createdAt", false);
        UUID driverId = personService.findIdByUserIdAuthenticateThrow();

        TripFilterRequest filterRequest = new TripFilterRequest();
        filterRequest.setDriverId(driverId);
        filterRequest.setDeleted(false);
        filterRequest.setTripStates(Set.of(TripStateEnum.ON_ROUTE, TripStateEnum.WAITING));

        Page<Trip> tripPage = tripService.findAllBySearch(filterRequest, pageable);

        if (tripPage.getTotalElements() > 0) {
            throw new TripException(
                "driver.already.trip.in.progress", "", HttpStatus.CONFLICT.value());
        }
    }

    @Override
    protected UUID run(Request request) {
        TripCreateRequest tripCreateRequest = request.tripCreateRequest;
        UUID driverId = personService.findIdByUserIdAuthenticateThrow();

        TripAssignmentDto tripAssignmentDto = tripAssignmentGetDtoCmd.withRequest(
                TripAssignmentGetDtoCmd.Request.builder()
                    .tripAssignmentId(tripCreateRequest.getTripAssignmentId())
                    .build())
            .execute();

        if (!tripAssignmentDto.getDriverId()
            .equals(driverId))
        {
            throw new TripException("driver.assignment.conflict", "", HttpStatus.CONFLICT.value());
        }

        ScheduleTransportationStateEnum scheduleTransportationStateCode = ScheduleTransportationStateEnum.fromValue(
            tripAssignmentDto.getScheduleTransportation()
                .getScheduleTransportationState()
                .getCode());

        if (ScheduleTransportationStateEnum.CANCELLED.equals(scheduleTransportationStateCode)
            || ScheduleTransportationStateEnum.FINALIZED.equals(scheduleTransportationStateCode))
        {
            throw new ScheduleTransportationException(
                "not.supported.state",
                "'" + scheduleTransportationStateCode + "'", HttpStatus.CONFLICT.value()
            );
        }

        updateScheduleStateToInProgress(
            scheduleTransportationStateCode, tripAssignmentDto.getScheduleTransportationId());

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

        emitWebSocketTripMessage(tripId);

        return tripId;
    }

    private void updateScheduleStateToInProgress(
        ScheduleTransportationStateEnum scheduleTransportationState, UUID scheduleTransportationId)
    {
        if (ScheduleTransportationStateEnum.IN_PROGRESS.equals(scheduleTransportationState)) {
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

        emitWebSocketScheduleMessage(scheduleTransportationId);
    }

    private void emitWebSocketScheduleMessage(UUID scheduleTransportationId) {
        ScheduleTransportation scheduleTransportation = scheduleTransportationService.findByIdThrow(
            scheduleTransportationId);

        webSocketHandler.emitMessage(
            WebSocketTopic.SCHEDULE_TRANSPORTATION_ON_PROGRESS, WebSocketSingleResponse.builder()
                .timestamp(ZonedDateTime.now())
                .data(scheduleTransportationResponseFlatMapper.toResponse(scheduleTransportation))
                .topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_ON_PROGRESS)
                .build()
        );
    }

    private void emitWebSocketTripMessage(UUID tripId) {
        Trip trip = tripService.findByIdThrow(tripId);

        webSocketHandler.emitMessage(
            WebSocketTopic.TRIP_STARTED, WebSocketSingleResponse.<TripResponse>builder()
                .timestamp(ZonedDateTime.now())
                .data(tripResponseMapper.toResponse(trip))
                .topic(WebSocketTopic.TRIP_STARTED)
                .build()
        );
    }

    @Builder
    public record Request(@NotNull TripCreateRequest tripCreateRequest) {

    }
}
