package com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip;

import com.kernotec.driverschedule.service.scheduling.jpa.entity.EmergencyResponseType;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip.EmergencyResponseTypeResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface EmergencyResponseTypeResponseMapper {

    EmergencyResponseTypeResponse toResponse(EmergencyResponseType emergencyResponseType);

    EmergencyResponseTypeResponse toResponse(UUID id);

    List<EmergencyResponseTypeResponse> toResponse(
        List<EmergencyResponseType> emergencyResponseTypeList);

    Set<EmergencyResponseTypeResponse> toResponse(
        Set<EmergencyResponseType> emergencyResponseTypeSet);
}
