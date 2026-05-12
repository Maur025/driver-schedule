package com.kernotec.driverschedule.service.resource.rest.mapper.response;

import com.kernotec.driverschedule.service.resource.jpa.entity.Vehicle;
import com.kernotec.driverschedule.service.resource.rest.dto.response.VehicleResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VehicleResponseFlatMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    VehicleResponse toResponse(Vehicle vehicle);

    VehicleResponse toResponse(UUID id);

    List<VehicleResponse> toResponse(List<Vehicle> vehicleList);

    Set<VehicleResponse> toResponse(Set<Vehicle> vehicleSet);
}
