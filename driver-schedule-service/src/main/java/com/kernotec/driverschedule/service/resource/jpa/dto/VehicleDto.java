package com.kernotec.driverschedule.service.resource.jpa.dto;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleDto extends AuditEntityDto {

    private String vehicleNumber;
    private String model;
    private Integer capacity;

    private UUID vehicleTypeId;
    private VehicleTypeDto vehicleType;
}
