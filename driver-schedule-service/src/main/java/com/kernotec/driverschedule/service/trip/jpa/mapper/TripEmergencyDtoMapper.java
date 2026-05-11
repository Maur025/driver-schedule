package com.kernotec.driverschedule.service.trip.jpa.mapper;

import com.kernotec.driverschedule.person.jpa.mapper.PersonDtoFlatMapper;
import com.kernotec.driverschedule.service.schedule.jpa.mapper.ScheduleToTripEmergencyDtoMapper;
import com.kernotec.driverschedule.service.trip.jpa.dto.TripEmergencyDto;
import com.kernotec.driverschedule.service.trip.jpa.entity.TripEmergency;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(
    uses = {TripDtoMapper.class, PersonDtoFlatMapper.class, ScheduleToTripEmergencyDtoMapper.class})
public interface TripEmergencyDtoMapper {

    TripEmergencyDto toDto(TripEmergency tripEmergency);

    List<TripEmergencyDto> toDto(List<TripEmergency> tripEmergencyList);

    Set<TripEmergencyDto> toDto(Set<TripEmergency> tripEmergencySet);
}
