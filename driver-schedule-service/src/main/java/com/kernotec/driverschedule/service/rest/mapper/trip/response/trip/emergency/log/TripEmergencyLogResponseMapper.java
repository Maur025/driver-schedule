package com.kernotec.driverschedule.service.rest.mapper.trip.response.trip.emergency.log;

import com.kernotec.driverschedule.service.jpa.entity.trip.TripEmergencyLog;
import com.kernotec.driverschedule.service.rest.dto.trip.response.trip.emergency.log.TripEmergencyLogResponse;
import com.kernotec.driverschedule.service.rest.mapper.trip.response.trip.emergency.TripEmergencyResponseMapper;
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
