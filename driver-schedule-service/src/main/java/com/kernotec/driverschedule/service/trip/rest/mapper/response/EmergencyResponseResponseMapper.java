package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.service.trip.jpa.entity.EmergencyResponse;
import com.kernotec.driverschedule.service.trip.rest.dto.response.EmergencyResponseResponse;
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
