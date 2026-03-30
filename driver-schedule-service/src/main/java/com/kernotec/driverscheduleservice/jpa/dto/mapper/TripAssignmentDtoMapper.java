package com.kernotec.driverscheduleservice.jpa.dto.mapper;

import com.kernotec.driverscheduleservice.jpa.dto.TripAssignmentDto;
import com.kernotec.driverscheduleservice.jpa.entity.TripAssignment;
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
