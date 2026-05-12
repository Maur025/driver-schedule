package com.kernotec.driverschedule.service.schedule.jpa.mapper;

import com.kernotec.driverschedule.service.schedule.jpa.dto.TripAssignmentDto;
import com.kernotec.driverschedule.person.jpa.mapper.PersonDtoFlatMapper;
import com.kernotec.driverschedule.service.resource.jpa.mapper.VehicleDtoFlatMapper;
import com.kernotec.driverschedule.service.schedule.jpa.entity.TripAssignment;
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
