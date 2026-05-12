package com.kernotec.driverschedule.service.trip.jpa.mapper;

import com.kernotec.driverschedule.service.trip.jpa.dto.TripDto;
import com.kernotec.driverschedule.service.schedule.jpa.mapper.TripAssignmentDtoFlatMapper;
import com.kernotec.driverschedule.service.trip.jpa.entity.Trip;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {TripAssignmentDtoFlatMapper.class})
public interface TripDtoMapper {

    TripDto toDto(Trip trip);

    List<TripDto> toDto(List<Trip> tripList);

    Set<TripDto> toDto(Set<Trip> tripSet);
}
