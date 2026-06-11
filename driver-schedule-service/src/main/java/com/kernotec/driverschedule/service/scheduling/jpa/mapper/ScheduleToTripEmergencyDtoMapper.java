package com.kernotec.driverschedule.service.scheduling.jpa.mapper;

import com.kernotec.driverschedule.service.scheduling.jpa.dto.ScheduleTransportationDto;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.ScheduleTransportation;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {TransportationRequestDtoFlatMapper.class})
public interface ScheduleToTripEmergencyDtoMapper {

    @Mapping(target = "tripAssignments", ignore = true)
    @Mapping(target = "personRequested", ignore = true)
    ScheduleTransportationDto toDto(ScheduleTransportation scheduleTransportation);

    List<ScheduleTransportationDto> toDto(List<ScheduleTransportation> scheduleTransportationList);

    Set<ScheduleTransportationDto> toDto(Set<ScheduleTransportation> scheduleTransportationSet);
}
