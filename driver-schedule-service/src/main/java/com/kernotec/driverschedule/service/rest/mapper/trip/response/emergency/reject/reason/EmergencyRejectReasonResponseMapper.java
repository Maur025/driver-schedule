package com.kernotec.driverschedule.service.rest.mapper.trip.response.emergency.reject.reason;

import com.kernotec.driverschedule.service.jpa.entity.trip.EmergencyRejectReason;
import com.kernotec.driverschedule.service.rest.dto.trip.response.emergency.reject.reason.EmergencyRejectReasonResponse;
import com.kernotec.driverschedule.service.rest.mapper.trip.response.trip.emergency.TripEmergencyResponseFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = TripEmergencyResponseFlatMapper.class)
public interface EmergencyRejectReasonResponseMapper {

    EmergencyRejectReasonResponse toResponse(EmergencyRejectReason emergencyRejectReason);

    List<EmergencyRejectReasonResponse> toResponse(
        List<EmergencyRejectReason> emergencyRejectReasonList);

    Set<EmergencyRejectReasonResponse> toResponse(
        Set<EmergencyRejectReason> emergencyRejectReasonSet);
}
