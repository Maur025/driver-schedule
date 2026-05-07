package com.kernotec.driverschedule.service.rest.mapper.trip.response.trip.emergency;

import com.kernotec.driverschedule.service.jpa.entity.trip.TripEmergency;
import com.kernotec.driverschedule.service.rest.dto.trip.response.trip.emergency.TripEmergencyResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TripEmergencyResponseFlatMapper {

    @Mapping(target = "personEmergencyReported", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "scheduleTransportation", ignore = true)
    @Mapping(target = "tripEmergencyState", ignore = true)
    @Mapping(target = "emergencyReasons", ignore = true)
    @Mapping(target = "emergencyRejectReasons", ignore = true)
    @Mapping(target = "emergencyResponses", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TripEmergencyResponse toResponse(TripEmergency tripEmergency);

    List<TripEmergencyResponse> toResponse(List<TripEmergency> tripEmergencyList);

    Set<TripEmergencyResponse> toResponse(Set<TripEmergency> tripEmergencySet);
}
