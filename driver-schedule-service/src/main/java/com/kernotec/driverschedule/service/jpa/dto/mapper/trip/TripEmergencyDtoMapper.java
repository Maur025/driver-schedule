package com.kernotec.driverschedule.service.jpa.dto.mapper.trip;

import com.kernotec.driverschedule.person.jpa.mapper.PersonDtoFlatMapper;
import com.kernotec.driverschedule.service.jpa.dto.trip.TripEmergencyDto;
import com.kernotec.driverschedule.service.jpa.entity.trip.TripEmergency;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {TripDtoMapper.class, PersonDtoFlatMapper.class})
public interface TripEmergencyDtoMapper {

    @Mapping(target = "scheduleTransportation", ignore = true)
    TripEmergencyDto toDto(TripEmergency tripEmergency);

    List<TripEmergencyDto> toDto(List<TripEmergency> tripEmergencyList);

    Set<TripEmergencyDto> toDto(Set<TripEmergency> tripEmergencySet);
}
