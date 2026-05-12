package com.kernotec.driverschedule.resource.jpa.mapper;

import com.kernotec.driverschedule.resource.jpa.dto.VehicleDto;
import com.kernotec.driverschedule.resource.jpa.entity.Vehicle;
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
