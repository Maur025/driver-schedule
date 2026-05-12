package com.kernotec.driverschedule.service.resource.rest.mapper.response;

import com.kernotec.driverschedule.common.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverschedule.service.resource.jpa.entity.Vehicle;
import com.kernotec.driverschedule.service.resource.rest.dto.response.VehicleResponse;
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
