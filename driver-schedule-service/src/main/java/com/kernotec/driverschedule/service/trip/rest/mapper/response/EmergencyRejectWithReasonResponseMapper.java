package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.service.trip.jpa.entity.EmergencyRejectReason;
import com.kernotec.driverschedule.service.trip.rest.dto.response.EmergencyRejectReasonResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EmergencyRejectWithReasonResponseMapper {

    @Mapping(target = "tripEmergency", ignore = true)
    EmergencyRejectReasonResponse toResponse(EmergencyRejectReason emergencyRejectReason);

    List<EmergencyRejectReasonResponse> toResponse(
        List<EmergencyRejectReason> emergencyRejectReasonList);

    Set<EmergencyRejectReasonResponse> toResponse(
        Set<EmergencyRejectReason> emergencyRejectReasonSet);
}
