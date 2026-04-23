package com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.emergency.log;

import com.kernotec.driverscheduleservice.jpa.entity.trip.TripEmergencyLog;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.emergency.log.TripEmergencyLogResponse;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.emergency.TripEmergencyResponseMapper;
import com.kernotec.driverscheduleservice.util.DateResponseUtil;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {TripEmergencyResponseMapper.class, DateResponseUtil.class})
public interface TripEmergencyLogResponseMapper {

    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    TripEmergencyLogResponse toResponse(TripEmergencyLog tripEmergencyLog);

    TripEmergencyLogResponse toResponse(UUID id);

    List<TripEmergencyLogResponse> toResponse(List<TripEmergencyLog> tripEmergencyLogList);

    Set<TripEmergencyLogResponse> toResponse(Set<TripEmergencyLog> tripEmergencyLogSet);
}
