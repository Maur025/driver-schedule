package com.kernotec.driverscheduleservice.rest.mapper.vehicle;

import com.kernotec.driverscheduleservice.jpa.entity.Vehicle;
import com.kernotec.driverscheduleservice.rest.dto.response.VehicleResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VehicleResponseFlatMapper {

    @Mapping(target = "vehicleType", ignore = true)
    VehicleResponse toResponse(Vehicle vehicle);

    VehicleResponse toResponse(UUID id);

    List<VehicleResponse> toResponse(List<Vehicle> vehicleList);

    Set<VehicleResponse> toResponse(Set<Vehicle> vehicleSet);
}
