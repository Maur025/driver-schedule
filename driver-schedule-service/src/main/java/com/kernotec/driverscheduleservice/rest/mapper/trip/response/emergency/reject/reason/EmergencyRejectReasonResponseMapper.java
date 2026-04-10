package com.kernotec.driverscheduleservice.rest.mapper.trip.response.emergency.reject.reason;

import com.kernotec.driverscheduleservice.jpa.entity.trip.EmergencyRejectReason;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.emergency.reject.reason.EmergencyRejectReasonResponse;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.emergency.TripEmergencyResponseMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {TripEmergencyResponseMapper.class})
public interface EmergencyRejectReasonResponseMapper {

    EmergencyRejectReasonResponse toResponse(EmergencyRejectReason emergencyRejectReason);

    List<EmergencyRejectReasonResponse> toResponse(
        List<EmergencyRejectReason> emergencyRejectReasonList);

    Set<EmergencyRejectReasonResponse> toResponse(
        Set<EmergencyRejectReason> emergencyRejectReasonSet);
}
