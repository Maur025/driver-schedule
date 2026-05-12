package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.service.common.util.GeoJsonUtil;
import com.kernotec.driverschedule.service.trip.jpa.entity.TripEmergency;
import com.kernotec.driverschedule.service.trip.rest.dto.response.TripEmergencyResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {GeoJsonUtil.class})
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
    @Mapping(target = "coordinates", source = "coordinate",
             qualifiedByName = "mapToPositionGeoJson")
    @Mapping(target = "longitude", source = "coordinate.lng")
    @Mapping(target = "latitude", source = "coordinate.lat")
    TripEmergencyResponse toResponse(TripEmergency tripEmergency);

    List<TripEmergencyResponse> toResponse(List<TripEmergency> tripEmergencyList);

    Set<TripEmergencyResponse> toResponse(Set<TripEmergency> tripEmergencySet);
}
