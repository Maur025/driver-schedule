package com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.emergency.log;

import com.kernotec.driverscheduleservice.jpa.entity.trip.TripEmergencyLog;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.emergency.log.TripEmergencyLogResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface TripEmergencyLogResponseMapper {

    TripEmergencyLogResponse toResponse(TripEmergencyLog tripEmergencyLog);

    TripEmergencyLogResponse toResponse(UUID id);

    List<TripEmergencyLogResponse> toResponse(List<TripEmergencyLog> tripEmergencyLogList);

    Set<TripEmergencyLogResponse> toResponse(Set<TripEmergencyLog> tripEmergencyLogSet);
}
