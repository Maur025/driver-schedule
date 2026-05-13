package com.kernotec.driverschedule.resource.rest.mapper.response;

import com.kernotec.driverschedule.resource.jpa.entity.VehicleType;
import com.kernotec.driverschedule.resource.rest.dto.response.VehicleTypeResponse;
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
