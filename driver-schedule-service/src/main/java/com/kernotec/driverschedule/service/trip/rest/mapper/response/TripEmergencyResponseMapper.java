package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.service.trip.jpa.entity.TripEmergency;
import com.kernotec.driverschedule.service.trip.rest.dto.response.TripEmergencyResponse;
import com.kernotec.driverschedule.person.rest.mapper.response.PersonResponseWithContactMapper;
import com.kernotec.driverschedule.service.schedule.rest.mapper.response.ScheduleTransportationToAvailabilityMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {TripResponseToEmergencyMapper.class, ScheduleTransportationToAvailabilityMapper.class,
    PersonResponseWithContactMapper.class, EmergencyReasonWithReasonResponseMapper.class,
    EmergencyRejectWithReasonResponseMapper.class, EmergencyResponseResponseToTripMapper.class,
    com.kernotec.driverschedule.common.mapping.DateResponseMapper.class})
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
