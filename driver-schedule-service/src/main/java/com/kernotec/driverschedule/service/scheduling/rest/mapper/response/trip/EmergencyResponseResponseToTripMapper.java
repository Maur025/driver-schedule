package com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip;

import com.kernotec.driverschedule.common.mapping.DateResponseMapper;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.EmergencyResponse;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip.EmergencyResponseResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {DateResponseMapper.class})
public interface EmergencyResponseResponseToTripMapper {

    @Mapping(target = "tripEmergency", ignore = true)
    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    EmergencyResponseResponse toResponse(EmergencyResponse emergencyResponse);

    List<EmergencyResponseResponse> toResponse(List<EmergencyResponse> emergencyResponseList);

    Set<EmergencyResponseResponse> toResponse(Set<EmergencyResponse> emergencyResponseSet);
}
