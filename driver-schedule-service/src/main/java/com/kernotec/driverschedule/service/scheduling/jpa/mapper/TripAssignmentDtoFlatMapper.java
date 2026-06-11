package com.kernotec.driverschedule.service.scheduling.jpa.mapper;

import com.kernotec.driverschedule.service.scheduling.jpa.dto.TripAssignmentDto;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripAssignment;
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
