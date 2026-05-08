package com.kernotec.driverschedule.service.jpa.dto.mapper.trip;

import com.kernotec.driverschedule.service.jpa.dto.trip.TripDto;
import com.kernotec.driverschedule.service.jpa.dto.mapper.schedule.trip.assignment.TripAssignmentDtoFlatMapper;
import com.kernotec.driverschedule.service.jpa.entity.trip.Trip;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {TripAssignmentDtoFlatMapper.class})
public interface TripDtoMapper {

    TripDto toDto(Trip trip);

    List<TripDto> toDto(List<Trip> tripList);

    Set<TripDto> toDto(Set<Trip> tripSet);
}
