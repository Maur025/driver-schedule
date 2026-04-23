package com.kernotec.driverscheduleservice.jpa.dto.mapper.trip;

import com.kernotec.driverscheduleservice.jpa.dto.trip.TripDto;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.schedule.trip.assignment.TripAssignmentDtoFlatMapper;
import com.kernotec.driverscheduleservice.jpa.entity.trip.Trip;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {TripAssignmentDtoFlatMapper.class})
public interface TripDtoMapper {

    TripDto toDto(Trip trip);

    List<TripDto> toDto(List<Trip> tripList);

    Set<TripDto> toDto(Set<Trip> tripSet);
}
