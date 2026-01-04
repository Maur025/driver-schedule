package com.kernotec.driverscheduleservice.rest.mapper.schedule.transportation;

import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.rest.dto.response.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.rest.mapper.person.PersonResponseFlatMapper;
import com.kernotec.driverscheduleservice.rest.mapper.transportation.request.TransportationRequestResponseFlatMapper;
import com.kernotec.driverscheduleservice.rest.mapper.vehicle.VehicleResponseMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {VehicleResponseMapper.class, PersonResponseFlatMapper.class,
    TransportationRequestResponseFlatMapper.class})
public interface ScheduleTransportationResponseMapper {

    ScheduleTransportationResponse toResponse(ScheduleTransportation scheduleTransportation);

    ScheduleTransportationResponse toResponse(UUID id);

    List<ScheduleTransportationResponse> toResponse(
        List<ScheduleTransportation> scheduleTransportationList);

    Set<ScheduleTransportationResponse> toResponse(
        Set<ScheduleTransportation> scheduleTransportationSet);
}
