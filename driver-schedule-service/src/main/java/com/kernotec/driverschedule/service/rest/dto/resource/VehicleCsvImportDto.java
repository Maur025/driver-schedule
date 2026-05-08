package com.kernotec.driverschedule.service.rest.dto.resource;

import com.kernotec.driverschedule.service.jpa.enums.resource.VehicleTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleCsvImportDto {

    private String vehicleNumber;
    private String model;
    private Integer capacity;
    private VehicleTypeEnum vehicleType;
}
