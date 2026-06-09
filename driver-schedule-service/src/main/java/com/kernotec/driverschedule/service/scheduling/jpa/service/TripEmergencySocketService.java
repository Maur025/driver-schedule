package com.kernotec.driverschedule.service.scheduling.jpa.service;

import com.kernotec.driverschedule.person.jpa.entity.Person;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
import com.kernotec.driverschedule.person.rest.mapper.response.PersonResponseWithContactMapper;
import com.kernotec.driverschedule.resource.jpa.entity.Reason;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.ScheduleTransportation;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.EmergencyReason;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.EmergencyRejectReason;
import com.kernotec.driverschedule.resource.jpa.service.ReasonService;
import com.kernotec.driverschedule.resource.rest.dto.response.ReasonResponse;
import com.kernotec.driverschedule.resource.rest.dto.response.WithReason;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.Trip;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripEmergency;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripEmergencyState;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip.EmergencyReasonResponse;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip.EmergencyRejectReasonResponse;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip.TripEmergencyResponse;
import com.kernotec.driverschedule.resource.rest.mapper.response.ReasonResponseFlatMapper;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.schedule.ScheduleTransportationToAvailabilityMapper;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip.EmergencyReasonWithReasonResponseMapper;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip.EmergencyRejectWithReasonResponseMapper;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip.TripResponseToEmergencyMapper;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip.TripEmergencyResponseMapper;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip.TripEmergencyStateResponseMapper;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripEmergencySocketService {

    private final TripEmergencyService tripEmergencyService;
    private final PersonService personService;
    private final TripService tripService;
    private final ScheduleTransportationService scheduleTransportationService;
    private final TripEmergencyStateService tripEmergencyStateService;
    private final EmergencyReasonService emergencyReasonService;
    private final EmergencyRejectReasonService emergencyRejectReasonService;
    private final ReasonService reasonService;

    private final TripEmergencyResponseMapper tripEmergencyResponseMapper;
    private final PersonResponseWithContactMapper personResponseWithContactMapper;
    private final ScheduleTransportationToAvailabilityMapper scheduleTransportationToAvailabilityMapper;
    private final TripEmergencyStateResponseMapper tripEmergencyStateResponseMapper;
    private final EmergencyReasonWithReasonResponseMapper emergencyReasonWithReasonResponseMapper;
    private final EmergencyRejectWithReasonResponseMapper emergencyRejectWithReasonResponseMapper;
    private final TripResponseToEmergencyMapper tripResponseToEmergencyMapper;
    private final ReasonResponseFlatMapper reasonResponseFlatMapper;


    public TripEmergencyResponse getResponseWithAllRelations(UUID tripEmergencyId) {
        TripEmergency tripEmergency = tripEmergencyService.findByIdThrow(tripEmergencyId);
        TripEmergencyResponse tripEmergencyResponse = tripEmergencyResponseMapper.toResponse(
            tripEmergency);

        addPersonInEmergencyResponse(tripEmergencyResponse);

        addTripInEmergencyResponse(tripEmergencyResponse);

        addScheduleInEmergencyResponse(tripEmergencyResponse);

        addEmergencyStateInEmergencyResponse(tripEmergencyResponse);

        addReasonsInEmergencyResponse(tripEmergencyResponse);

        addRejectReasonInEmergencyResponse(tripEmergencyResponse);

        return tripEmergencyResponse;
    }

    private void addPersonInEmergencyResponse(TripEmergencyResponse tripEmergencyResponse) {
        Person person = personService.findByIdThrow(
            tripEmergencyResponse.getPersonEmergencyReportedId());

        tripEmergencyResponse.setPersonEmergencyReported(
            personResponseWithContactMapper.toResponse(person));
    }

    private void addTripInEmergencyResponse(TripEmergencyResponse tripEmergencyResponse) {
        Trip trip = tripService.findByIdThrow(tripEmergencyResponse.getTripId());

        tripEmergencyResponse.setTrip(tripResponseToEmergencyMapper.toResponse(trip));
    }

    private void addScheduleInEmergencyResponse(TripEmergencyResponse tripEmergencyResponse) {
        ScheduleTransportation scheduleTransportation = scheduleTransportationService.findByIdThrow(
            tripEmergencyResponse.getScheduleTransportationId());

        tripEmergencyResponse.setScheduleTransportation(
            scheduleTransportationToAvailabilityMapper.toResponse(scheduleTransportation));
    }

    private void addEmergencyStateInEmergencyResponse(TripEmergencyResponse tripEmergencyResponse) {
        TripEmergencyState tripEmergencyState = tripEmergencyStateService.findByIdThrow(
            tripEmergencyResponse.getTripEmergencyStateId());

        tripEmergencyResponse.setTripEmergencyState(
            tripEmergencyStateResponseMapper.toResponse(tripEmergencyState));
    }

    private void addReasonsInEmergencyResponse(TripEmergencyResponse tripEmergencyResponse) {
        List<EmergencyReason> emergencyReasonList = emergencyReasonService.findByTripEmergencyId(
            tripEmergencyResponse.getId());

        List<EmergencyReasonResponse> emergencyReasonResponseList = emergencyReasonWithReasonResponseMapper.toResponse(
            emergencyReasonList);

        setReasonsInResponse(emergencyReasonResponseList);

        tripEmergencyResponse.setEmergencyReasons(emergencyReasonResponseList);
    }

    private void addRejectReasonInEmergencyResponse(TripEmergencyResponse tripEmergencyResponse) {
        List<EmergencyRejectReason> emergencyRejectReasonList = emergencyRejectReasonService.findByTripEmergencyId(
            tripEmergencyResponse.getId());

        List<EmergencyRejectReasonResponse> emergencyRejectReasonResponseList = emergencyRejectWithReasonResponseMapper.toResponse(
            emergencyRejectReasonList);

        setReasonsInResponse(emergencyRejectReasonResponseList);

        tripEmergencyResponse.setEmergencyRejectReasons(emergencyRejectReasonResponseList);
    }

    private <T extends WithReason> void setReasonsInResponse(List<T> responseList)
    {
        Set<UUID> reasonIds = responseList.stream()
            .map(WithReason::getReasonId)
            .collect(Collectors.toSet());

        List<Reason> reasonList = reasonService.findByIdIn(reasonIds);

        Map<UUID, ReasonResponse> reasonResponseMap = reasonResponseFlatMapper.toResponse(
                reasonList)
            .stream()
            .collect(Collectors.toMap(ReasonResponse::getId, reasonResponse -> reasonResponse));

        responseList.forEach(emergencyReasonResponse -> emergencyReasonResponse.setReason(
            reasonResponseMap.get(emergencyReasonResponse.getReasonId())));
    }
}
