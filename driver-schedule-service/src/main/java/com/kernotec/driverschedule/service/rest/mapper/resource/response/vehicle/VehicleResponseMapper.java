package com.kernotec.driverschedule.service.rest.mapper.resource.response.vehicle;

import com.kernotec.driverschedule.common.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverschedule.service.jpa.entity.resource.Vehicle;
import com.kernotec.driverschedule.service.rest.dto.resource.response.vehicle.VehicleResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {AuthUserDataResponseMapper.class, com.kernotec.driverschedule.common.mapping.DateResponseMapper.class})
public interface VehicleResponseMapper {

    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    VehicleResponse toResponse(Vehicle vehicle);

    VehicleResponse toResponse(UUID id);

    List<VehicleResponse> toResponse(List<Vehicle> vehicleList);

    Set<VehicleResponse> toResponse(Set<Vehicle> vehicleSet);
}
