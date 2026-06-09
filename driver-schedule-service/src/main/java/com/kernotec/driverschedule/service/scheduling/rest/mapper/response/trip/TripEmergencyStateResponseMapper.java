package com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip;

import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripEmergencyState;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip.TripEmergencyStateResponse;
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
