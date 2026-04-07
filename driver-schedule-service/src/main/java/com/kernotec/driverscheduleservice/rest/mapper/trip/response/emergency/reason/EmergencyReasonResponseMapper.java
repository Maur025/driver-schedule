package com.kernotec.driverscheduleservice.rest.mapper.trip.response.emergency.reason;

import com.kernotec.driverscheduleservice.jpa.entity.trip.EmergencyReason;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.emergency.reason.EmergencyReasonResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface EmergencyReasonResponseMapper {

    EmergencyReasonResponse toResponse(EmergencyReason emergencyReason);

    EmergencyReasonResponse toResponse(UUID id);

    List<EmergencyReasonResponse> toResponse(List<EmergencyReason> emergencyReasonList);

    Set<EmergencyReasonResponse> toResponse(Set<EmergencyReason> emergencyReasonSet);
}
