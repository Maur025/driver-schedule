package com.kernotec.driverscheduleservice.jpa.dto.mapper.resource;

import com.kernotec.driverscheduleservice.jpa.dto.resource.VehicleDto;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Vehicle;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VehicleDtoFlatMapper {

    @Mapping(target = "vehicleType", ignore = true)
    VehicleDto toDto(Vehicle vehicle);

    List<VehicleDto> toDto(List<Vehicle> vehicleList);

    Set<VehicleDto> toDto(Set<Vehicle> vehicleSet);
}
