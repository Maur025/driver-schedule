package com.kernotec.driverschedule.service.rest.mapper.trip.response.emergency.reason;

import com.kernotec.driverschedule.service.jpa.entity.trip.EmergencyReason;
import com.kernotec.driverschedule.service.rest.dto.trip.response.emergency.reason.EmergencyReasonResponse;
import com.kernotec.driverschedule.service.rest.mapper.trip.response.trip.emergency.TripEmergencyResponseFlatMapper;
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
