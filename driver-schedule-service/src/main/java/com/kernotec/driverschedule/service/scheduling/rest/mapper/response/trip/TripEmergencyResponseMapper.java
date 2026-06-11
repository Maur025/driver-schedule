package com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip;

import com.kernotec.driverschedule.common.mapping.DateResponseMapper;
import com.kernotec.driverschedule.common.util.GeoJsonUtil;
import com.kernotec.driverschedule.person.rest.mapper.response.PersonResponseWithContactMapper;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripEmergency;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip.TripEmergencyResponse;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.schedule.ScheduleTransportationToAvailabilityMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    uses = {TripResponseToEmergencyMapper.class, ScheduleTransportationToAvailabilityMapper.class,
        PersonResponseWithContactMapper.class, EmergencyReasonWithReasonResponseMapper.class,
        EmergencyRejectWithReasonResponseMapper.class, EmergencyResponseResponseToTripMapper.class,
        DateResponseMapper.class, GeoJsonUtil.class})
public interface TripEmergencyResponseMapper {

    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "coordinates", source = "coordinate",
             qualifiedByName = "mapToPositionGeoJson")
    @Mapping(target = "longitude", source = "coordinate.lng")
    @Mapping(target = "latitude", source = "coordinate.lat")
    TripEmergencyResponse toResponse(TripEmergency tripEmergency);

    TripEmergencyResponse toResponse(UUID id);

    List<TripEmergencyResponse> toResponse(List<TripEmergency> tripEmergencyList);

    Set<TripEmergencyResponse> toResponse(Set<TripEmergency> tripEmergencySet);
}
