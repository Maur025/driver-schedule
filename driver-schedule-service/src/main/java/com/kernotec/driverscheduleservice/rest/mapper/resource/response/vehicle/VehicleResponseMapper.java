package com.kernotec.driverscheduleservice.rest.mapper.resource.response.vehicle;

import com.kernotec.driverscheduleservice.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Vehicle;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.vehicle.VehicleResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {AuthUserDataResponseMapper.class})
public interface VehicleResponseMapper {

    VehicleResponse toResponse(UUID id);

    VehicleResponse toResponse(Vehicle vehicle);

    List<VehicleResponse> toResponse(List<Vehicle> vehicleList);

    Set<VehicleResponse> toResponse(Set<Vehicle> vehicleSet);
}
