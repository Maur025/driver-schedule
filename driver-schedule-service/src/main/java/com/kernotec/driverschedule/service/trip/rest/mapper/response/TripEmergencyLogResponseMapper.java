package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.service.trip.jpa.entity.TripEmergencyLog;
import com.kernotec.driverschedule.service.trip.rest.dto.response.TripEmergencyLogResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {TripEmergencyResponseMapper.class, com.kernotec.driverschedule.common.mapping.DateResponseMapper.class})
public interface TripEmergencyLogResponseMapper {

    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    TripEmergencyLogResponse toResponse(TripEmergencyLog tripEmergencyLog);

    TripEmergencyLogResponse toResponse(UUID id);

    List<TripEmergencyLogResponse> toResponse(List<TripEmergencyLog> tripEmergencyLogList);

    Set<TripEmergencyLogResponse> toResponse(Set<TripEmergencyLog> tripEmergencyLogSet);
}
