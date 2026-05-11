package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.service.trip.jpa.entity.EmergencyResponse;
import com.kernotec.driverschedule.service.trip.rest.dto.response.EmergencyResponseResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EmergencyResponseResponseToTripMapper {

    @Mapping(target = "tripEmergency", ignore = true)
    EmergencyResponseResponse toResponse(EmergencyResponse emergencyResponse);

    List<EmergencyResponseResponse> toResponse(List<EmergencyResponse> emergencyResponseList);

    Set<EmergencyResponseResponse> toResponse(Set<EmergencyResponse> emergencyResponseSet);
}
