package com.kernotec.driverschedule.service.jpa.dto.mapper.schedule.trip.assignment;

import com.kernotec.driverschedule.service.jpa.dto.mapper.trip.TripDtoFlatMapper;
import com.kernotec.driverschedule.service.jpa.dto.schedule.TripAssignmentDto;
import com.kernotec.driverschedule.service.jpa.entity.schedule.TripAssignment;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = TripDtoFlatMapper.class)
public interface TripAssignmentDtoFlatMapper {

    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "scheduleTransportation", ignore = true)
    TripAssignmentDto toDto(TripAssignment tripAssignment);

    List<TripAssignmentDto> toDto(List<TripAssignment> tripAssignmentList);

    Set<TripAssignmentDto> toDto(Set<TripAssignment> tripAssignmentSet);
}
