package com.kernotec.driverschedule.service.rest.mapper.trip.response.trip.log;

import com.kernotec.driverschedule.common.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverschedule.service.jpa.entity.trip.TripLog;
import com.kernotec.driverschedule.service.rest.dto.trip.response.trip.log.TripLogResponse;
import com.kernotec.driverschedule.service.rest.mapper.trip.response.trip.TripNAResponseFlatMapper;
import com.kernotec.driverschedule.service.util.GeoJsonUtil;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {TripNAResponseFlatMapper.class, AuthUserDataResponseMapper.class, GeoJsonUtil.class,
    com.kernotec.driverschedule.common.mapping.DateResponseMapper.class})
public interface TripLogResponseMapper {

    @Mapping(target = "coordinates", source = "coordinate",
             qualifiedByName = "mapToPositionGeoJson")
    @Mapping(target = "longitude", source = "coordinate.lng")
    @Mapping(target = "latitude", source = "coordinate.lat")
    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    TripLogResponse toResponse(TripLog tripLog);

    TripLogResponse toResponse(UUID id);

    List<TripLogResponse> toResponse(List<TripLog> tripLogList);

    Set<TripLogResponse> toResponse(Set<TripLog> tripLogSet);
}
