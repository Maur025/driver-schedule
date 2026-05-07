package com.kernotec.driverscheduleservice.jpa.dto.mapper.schedule;

import com.kernotec.driverscheduleservice.jpa.dto.schedule.ScheduleTransportationDto;
import com.kernotec.driverscheduleservice.request.jpa.mapper.TransportationRequestDtoFlatMapper;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.schedule.trip.assignment.TripAssignmentDtoFlatMapper;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportation;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {TransportationRequestDtoFlatMapper.class, TripAssignmentDtoFlatMapper.class})
public interface ScheduleTransportationDtoMapper {

    ScheduleTransportationDto toDto(ScheduleTransportation scheduleTransportation);

    List<ScheduleTransportationDto> toDto(List<ScheduleTransportation> scheduleTransportationList);

    Set<ScheduleTransportationDto> toDto(Set<ScheduleTransportation> scheduleTransportationSet);
}
