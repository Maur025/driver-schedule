package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.service.trip.jpa.entity.EmergencyReason;
import com.kernotec.driverschedule.service.trip.rest.dto.response.EmergencyReasonResponse;
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
