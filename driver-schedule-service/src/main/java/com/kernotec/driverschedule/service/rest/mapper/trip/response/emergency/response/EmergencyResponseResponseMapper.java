package com.kernotec.driverschedule.service.rest.mapper.trip.response.emergency.response;

import com.kernotec.driverschedule.service.jpa.entity.trip.EmergencyResponse;
import com.kernotec.driverschedule.service.rest.dto.trip.response.emergency.response.EmergencyResponseResponse;
import com.kernotec.driverschedule.service.rest.mapper.trip.response.trip.emergency.TripEmergencyResponseMapper;
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
