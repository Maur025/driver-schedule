package com.kernotec.driverschedule.service.jpa.dto.mapper.trip;

import com.kernotec.driverschedule.service.jpa.dto.trip.TripDto;
import com.kernotec.driverschedule.service.jpa.entity.trip.Trip;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TripDtoFlatMapper {

    @Mapping(target = "tripAssignment", ignore = true)
    @Mapping(target = "tripState", ignore = true)
    TripDto toDto(Trip trip);

    List<TripDto> toDto(List<Trip> tripList);

    Set<TripDto> toDto(Set<Trip> tripSet);
}
