package com.kernotec.driverschedule.service.rest.mapper.trip.response.emergency.reason;

import com.kernotec.driverschedule.service.jpa.entity.trip.EmergencyReason;
import com.kernotec.driverschedule.service.rest.dto.trip.response.emergency.reason.EmergencyReasonResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EmergencyReasonWithReasonResponseMapper {

    @Mapping(target = "tripEmergency", ignore = true)
    EmergencyReasonResponse toResponse(EmergencyReason emergencyReason);

    List<EmergencyReasonResponse> toResponse(List<EmergencyReason> emergencyReasonList);

    Set<EmergencyReasonResponse> toResponse(Set<EmergencyReason> emergencyReasonSet);
}
