package com.kernotec.driverscheduleservice.jpa.dto.mapper;

import com.kernotec.driverscheduleservice.jpa.dto.ScheduleTransportationDto;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.transportation.request.TransportationRequestDtoFlatMapper;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.trip.assignment.TripAssignmentDtoFlatMapper;
import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {TransportationRequestDtoFlatMapper.class, TripAssignmentDtoFlatMapper.class})
public interface ScheduleTransportationDtoMapper {

    ScheduleTransportationDto toDto(ScheduleTransportation scheduleTransportation);

    List<ScheduleTransportationDto> toDto(List<ScheduleTransportation> scheduleTransportationList);

    Set<ScheduleTransportationDto> toDto(Set<ScheduleTransportation> scheduleTransportationSet);
}
