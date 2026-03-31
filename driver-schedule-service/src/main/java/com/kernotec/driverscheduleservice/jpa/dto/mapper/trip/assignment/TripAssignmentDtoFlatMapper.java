package com.kernotec.driverscheduleservice.jpa.dto.mapper.trip.assignment;

import com.kernotec.driverscheduleservice.jpa.dto.TripAssignmentDto;
import com.kernotec.driverscheduleservice.jpa.entity.TripAssignment;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TripAssignmentDtoFlatMapper {

    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "scheduleTransportation", ignore = true)
    TripAssignmentDto toDto(TripAssignment tripAssignment);

    List<TripAssignmentDto> toDto(List<TripAssignment> tripAssignmentList);

    Set<TripAssignmentDto> toDto(Set<TripAssignment> tripAssignmentSet);
}
