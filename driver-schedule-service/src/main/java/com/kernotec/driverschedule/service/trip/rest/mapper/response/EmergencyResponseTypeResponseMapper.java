package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.service.trip.jpa.entity.EmergencyResponseType;
import com.kernotec.driverschedule.service.trip.rest.dto.response.EmergencyResponseTypeResponse;
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
