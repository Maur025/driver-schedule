package com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.emergency;

import com.kernotec.driverscheduleservice.jpa.entity.trip.TripEmergency;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.emergency.TripEmergencyResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.person.PersonResponseWithContactMapper;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationToAvailabilityMapper;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.emergency.reason.EmergencyReasonWithReasonResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.emergency.reject.reason.EmergencyRejectWithReasonResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.emergency.response.EmergencyResponseResponseToTripMapper;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.TripResponseFlatMapper;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.TripResponseToEmergencyMapper;
import com.kernotec.driverscheduleservice.util.DateResponseUtil;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {TripResponseToEmergencyMapper.class, ScheduleTransportationToAvailabilityMapper.class,
    PersonResponseWithContactMapper.class, EmergencyReasonWithReasonResponseMapper.class,
    EmergencyRejectWithReasonResponseMapper.class, EmergencyResponseResponseToTripMapper.class,
    DateResponseUtil.class})
public interface TripEmergencyResponseMapper {

    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    TripEmergencyResponse toResponse(TripEmergency tripEmergency);

    TripEmergencyResponse toResponse(UUID id);

    List<TripEmergencyResponse> toResponse(List<TripEmergency> tripEmergencyList);

    Set<TripEmergencyResponse> toResponse(Set<TripEmergency> tripEmergencySet);
}
