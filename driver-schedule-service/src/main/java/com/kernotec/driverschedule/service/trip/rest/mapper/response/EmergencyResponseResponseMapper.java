package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.common.mapping.DateResponseMapper;
import com.kernotec.driverschedule.service.trip.jpa.entity.EmergencyResponse;
import com.kernotec.driverschedule.service.trip.rest.dto.response.EmergencyResponseResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {TripEmergencyResponseMapper.class, DateResponseMapper.class})
public interface EmergencyResponseResponseMapper {

    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    EmergencyResponseResponse toResponse(EmergencyResponse emergencyResponse);

    EmergencyResponseResponse toResponse(UUID id);

    List<EmergencyResponseResponse> toResponse(List<EmergencyResponse> emergencyResponseList);

    Set<EmergencyResponseResponse> toResponse(Set<EmergencyResponse> emergencyResponseSet);
}
