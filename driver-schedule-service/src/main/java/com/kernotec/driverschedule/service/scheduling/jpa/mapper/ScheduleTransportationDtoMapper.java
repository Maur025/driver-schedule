package com.kernotec.driverschedule.service.scheduling.jpa.mapper;

import com.kernotec.driverschedule.service.scheduling.jpa.dto.ScheduleTransportationDto;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.ScheduleTransportation;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {TransportationRequestDtoFlatMapper.class, TripAssignmentDtoWithDriverMapper.class})
public interface ScheduleTransportationDtoMapper {

    ScheduleTransportationDto toDto(ScheduleTransportation scheduleTransportation);

    List<ScheduleTransportationDto> toDto(List<ScheduleTransportation> scheduleTransportationList);

    Set<ScheduleTransportationDto> toDto(Set<ScheduleTransportation> scheduleTransportationSet);
}
