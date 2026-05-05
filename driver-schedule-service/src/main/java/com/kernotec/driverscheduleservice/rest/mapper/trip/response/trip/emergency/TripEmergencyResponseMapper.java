package com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.emergency;

import com.kernotec.driverscheduleservice.jpa.entity.trip.TripEmergency;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.emergency.TripEmergencyResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.person.PersonResponseWithContactMapper;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationToAvailabilityMapper;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.emergency.reason.EmergencyReasonWithReasonResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.emergency.reject.reason.EmergencyRejectWithReasonResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.emergency.response.EmergencyResponseResponseToTripMapper;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.TripResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {TripResponseFlatMapper.class, ScheduleTransportationToAvailabilityMapper.class,
    PersonResponseWithContactMapper.class, EmergencyReasonWithReasonResponseMapper.class,
    EmergencyRejectWithReasonResponseMapper.class, EmergencyResponseResponseToTripMapper.class})
public interface TripEmergencyResponseMapper {

    TripEmergencyResponse toResponse(TripEmergency tripEmergency);

    TripEmergencyResponse toResponse(UUID id);

    List<TripEmergencyResponse> toResponse(List<TripEmergency> tripEmergencyList);

    Set<TripEmergencyResponse> toResponse(Set<TripEmergency> tripEmergencySet);
}
