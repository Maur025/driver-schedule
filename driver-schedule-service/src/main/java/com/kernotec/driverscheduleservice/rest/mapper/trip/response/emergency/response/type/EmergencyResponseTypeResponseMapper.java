package com.kernotec.driverscheduleservice.rest.mapper.trip.response.emergency.response.type;

import com.kernotec.driverscheduleservice.jpa.entity.trip.EmergencyResponseType;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.emergency.response.type.EmergencyResponseTypeResponse;
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
