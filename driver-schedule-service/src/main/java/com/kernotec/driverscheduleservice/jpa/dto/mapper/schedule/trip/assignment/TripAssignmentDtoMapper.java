package com.kernotec.driverscheduleservice.jpa.dto.mapper.schedule.trip.assignment;

import com.kernotec.driverscheduleservice.jpa.dto.schedule.TripAssignmentDto;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.resource.PersonDtoFlatMapper;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.schedule.ScheduleTransportationDtoMapper;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.resource.VehicleDtoFlatMapper;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.TripAssignment;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {VehicleDtoFlatMapper.class, PersonDtoFlatMapper.class,
    ScheduleTransportationDtoMapper.class})
public interface TripAssignmentDtoMapper {

    TripAssignmentDto toDto(TripAssignment tripAssignment);

    List<TripAssignmentDto> toDto(List<TripAssignment> tripAssignmentList);

    Set<TripAssignmentDto> toDto(Set<TripAssignment> tripAssignmentSet);
}
