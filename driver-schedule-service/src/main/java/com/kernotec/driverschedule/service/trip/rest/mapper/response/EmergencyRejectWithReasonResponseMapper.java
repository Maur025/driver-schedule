package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.common.mapping.DateResponseMapper;
import com.kernotec.driverschedule.service.trip.jpa.entity.EmergencyRejectReason;
import com.kernotec.driverschedule.service.trip.rest.dto.response.EmergencyRejectReasonResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {DateResponseMapper.class})
public interface EmergencyRejectWithReasonResponseMapper {

    @Mapping(target = "tripEmergency", ignore = true)
    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    EmergencyRejectReasonResponse toResponse(EmergencyRejectReason emergencyRejectReason);

    List<EmergencyRejectReasonResponse> toResponse(
        List<EmergencyRejectReason> emergencyRejectReasonList);

    Set<EmergencyRejectReasonResponse> toResponse(
        Set<EmergencyRejectReason> emergencyRejectReasonSet);
}
