package com.kernotec.driverschedule.service.rest.mapper.trip.response.trip.emergency.state;

import com.kernotec.driverschedule.service.jpa.entity.trip.TripEmergencyState;
import com.kernotec.driverschedule.service.rest.dto.trip.response.trip.emergency.state.TripEmergencyStateResponse;
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
