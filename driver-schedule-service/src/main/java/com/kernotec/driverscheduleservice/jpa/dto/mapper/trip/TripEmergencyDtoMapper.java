package com.kernotec.driverscheduleservice.jpa.dto.mapper.trip;

import com.kernotec.driverscheduleservice.jpa.dto.trip.TripEmergencyDto;
import com.kernotec.driverscheduleservice.jpa.entity.trip.TripEmergency;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {TripDtoMapper.class})
public interface TripEmergencyDtoMapper {

    @Mapping(target = "personEmergencyReported", ignore = true)
    @Mapping(target = "scheduleTransportation", ignore = true)
    TripEmergencyDto toDto(TripEmergency tripEmergency);

    List<TripEmergencyDto> toDto(List<TripEmergency> tripEmergencyList);

    Set<TripEmergencyDto> toDto(Set<TripEmergency> tripEmergencySet);
}
