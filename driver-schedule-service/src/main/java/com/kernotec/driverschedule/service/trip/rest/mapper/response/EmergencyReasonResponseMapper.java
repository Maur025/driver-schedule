package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.service.trip.jpa.entity.EmergencyReason;
import com.kernotec.driverschedule.service.trip.rest.dto.response.EmergencyReasonResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = TripEmergencyResponseFlatMapper.class)
public interface EmergencyReasonResponseMapper {

    EmergencyReasonResponse toResponse(EmergencyReason emergencyReason);

    EmergencyReasonResponse toResponse(UUID id);

    List<EmergencyReasonResponse> toResponse(List<EmergencyReason> emergencyReasonList);

    Set<EmergencyReasonResponse> toResponse(Set<EmergencyReason> emergencyReasonSet);
}
