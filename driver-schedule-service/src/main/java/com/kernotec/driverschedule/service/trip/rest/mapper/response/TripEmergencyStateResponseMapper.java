package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.service.trip.jpa.entity.TripEmergencyState;
import com.kernotec.driverschedule.service.trip.rest.dto.response.TripEmergencyStateResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface TripEmergencyStateResponseMapper {

    TripEmergencyStateResponse toResponse(TripEmergencyState tripEmergencyState);

    TripEmergencyStateResponse toResponse(UUID id);

    List<TripEmergencyStateResponse> toResponse(List<TripEmergencyState> tripEmergencyStateList);

    Set<TripEmergencyStateResponse> toResponse(Set<TripEmergencyState> tripEmergencyStateSet);
}
