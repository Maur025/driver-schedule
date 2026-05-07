package com.kernotec.driverschedule.service.rest.mapper.schedule.response.schedule.transportation;

import com.kernotec.driverschedule.common.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverschedule.service.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverschedule.service.rest.dto.schedule.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverschedule.service.request.rest.mapper.response.TransportationReqToScheduleResponseMapper;
import com.kernotec.driverschedule.person.rest.mapper.response.PersonResponseWithContactMapper;
import com.kernotec.driverschedule.service.rest.mapper.schedule.response.cancel.reason.CancelReasonWithReasonResponseMapper;
import com.kernotec.driverschedule.service.rest.mapper.schedule.response.reschedule.reason.RescheduleReasonWithReasonResponseMapper;
import com.kernotec.driverschedule.service.rest.mapper.schedule.response.trip.assignment.TripAssignmentToScheduleResponseMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    uses = {PersonResponseWithContactMapper.class, TransportationReqToScheduleResponseMapper.class,
        AuthUserDataResponseMapper.class, TripAssignmentToScheduleResponseMapper.class,
        CancelReasonWithReasonResponseMapper.class, RescheduleReasonWithReasonResponseMapper.class,
        com.kernotec.driverschedule.common.mapping.DateResponseMapper.class})
public interface ScheduleTransportationResponseMapper {

    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    ScheduleTransportationResponse toResponse(ScheduleTransportation scheduleTransportation);

    ScheduleTransportationResponse toResponse(UUID id);

    List<ScheduleTransportationResponse> toResponse(
        List<ScheduleTransportation> scheduleTransportationList);

    Set<ScheduleTransportationResponse> toResponse(
        Set<ScheduleTransportation> scheduleTransportationSet);
}
