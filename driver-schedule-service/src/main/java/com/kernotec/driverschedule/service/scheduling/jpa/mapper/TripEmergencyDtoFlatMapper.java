package com.kernotec.driverschedule.service.scheduling.jpa.mapper;

import com.kernotec.driverschedule.service.scheduling.jpa.dto.TripEmergencyDto;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripEmergency;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TripEmergencyDtoFlatMapper {

    @Mapping(target = "personEmergencyReported", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "scheduleTransportation", ignore = true)
    @Mapping(target = "tripEmergencyState", ignore = true)
    TripEmergencyDto toDto(TripEmergency tripEmergency);

    List<TripEmergencyDto> toDto(List<TripEmergency> tripEmergencyList);

    Set<TripEmergencyDto> toDto(Set<TripEmergency> tripEmergencySet);
}
