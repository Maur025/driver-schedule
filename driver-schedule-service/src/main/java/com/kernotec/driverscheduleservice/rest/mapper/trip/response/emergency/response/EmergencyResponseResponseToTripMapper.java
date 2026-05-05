package com.kernotec.driverscheduleservice.rest.mapper.trip.response.emergency.response;

import com.kernotec.driverscheduleservice.jpa.entity.trip.EmergencyResponse;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.emergency.response.EmergencyResponseResponse;
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
