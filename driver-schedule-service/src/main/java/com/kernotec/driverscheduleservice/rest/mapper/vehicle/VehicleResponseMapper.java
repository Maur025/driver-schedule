package com.kernotec.driverscheduleservice.rest.mapper.vehicle;

import com.kernotec.driverscheduleservice.jpa.entity.Vehicle;
import com.kernotec.driverscheduleservice.rest.dto.response.VehicleResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface VehicleResponseMapper {

    VehicleResponse toResponse(UUID id);

    VehicleResponse toResponse(Vehicle vehicle);

    List<VehicleResponse> toResponse(List<Vehicle> vehicleList);

    Set<VehicleResponse> toResponse(Set<Vehicle> vehicleSet);
}
