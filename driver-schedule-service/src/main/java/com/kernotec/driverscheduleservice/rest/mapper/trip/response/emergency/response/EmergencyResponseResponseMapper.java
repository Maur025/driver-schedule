package com.kernotec.driverscheduleservice.rest.mapper.trip.response.emergency.response;

import com.kernotec.driverscheduleservice.jpa.entity.trip.EmergencyResponse;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.emergency.response.EmergencyResponseResponse;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.emergency.TripEmergencyResponseMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {TripEmergencyResponseMapper.class})
public interface EmergencyResponseResponseMapper {

    EmergencyResponseResponse toResponse(EmergencyResponse emergencyResponse);

    EmergencyResponseResponse toResponse(UUID id);

    List<EmergencyResponseResponse> toResponse(List<EmergencyResponse> emergencyResponseList);

    Set<EmergencyResponseResponse> toResponse(Set<EmergencyResponse> emergencyResponseSet);
}
