package com.kernotec.driverscheduleservice.rest.mapper.resource.response.vehicle.type;

import com.kernotec.driverscheduleservice.jpa.entity.resource.VehicleType;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.vehicle.type.VehicleTypeResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface VehicleTypeResponseMapper {

    VehicleTypeResponse toResponse(UUID id);

    VehicleTypeResponse toResponse(VehicleType vehicleType);

    List<VehicleTypeResponse> toResponse(List<VehicleType> vehicleTypeList);

    Set<VehicleTypeResponse> toResponse(Set<VehicleType> vehicleTypeSet);
}
