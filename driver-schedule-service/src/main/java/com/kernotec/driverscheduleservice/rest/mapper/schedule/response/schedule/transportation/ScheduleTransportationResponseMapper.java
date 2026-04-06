package com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation;

import com.kernotec.driverscheduleservice.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.cancel.reason.CancelReasonWithReasonResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.person.PersonResponseWithContactMapper;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.reschedule.reason.RescheduleReasonWithReasonResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.request.response.transportation.request.TransportationReqToScheduleResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.trip.assignment.TripAssignmentToScheduleResponseMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(
    uses = {PersonResponseWithContactMapper.class, TransportationReqToScheduleResponseMapper.class,
        AuthUserDataResponseMapper.class, TripAssignmentToScheduleResponseMapper.class,
        CancelReasonWithReasonResponseMapper.class, RescheduleReasonWithReasonResponseMapper.class})
public interface ScheduleTransportationResponseMapper {

    ScheduleTransportationResponse toResponse(ScheduleTransportation scheduleTransportation);

    ScheduleTransportationResponse toResponse(UUID id);

    List<ScheduleTransportationResponse> toResponse(
        List<ScheduleTransportation> scheduleTransportationList);

    Set<ScheduleTransportationResponse> toResponse(
        Set<ScheduleTransportation> scheduleTransportationSet);
}
