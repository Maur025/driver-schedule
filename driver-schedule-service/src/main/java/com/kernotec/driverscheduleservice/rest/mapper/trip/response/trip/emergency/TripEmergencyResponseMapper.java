package com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.emergency;

import com.kernotec.driverscheduleservice.jpa.entity.trip.TripEmergency;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.emergency.TripEmergencyResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface TripEmergencyResponseMapper {

    TripEmergencyResponse toResponse(TripEmergency tripEmergency);

    TripEmergencyResponse toResponse(UUID id);

    List<TripEmergencyResponse> toResponse(List<TripEmergency> tripEmergencyList);

    Set<TripEmergencyResponse> toResponse(Set<TripEmergency> tripEmergencySet);
}
