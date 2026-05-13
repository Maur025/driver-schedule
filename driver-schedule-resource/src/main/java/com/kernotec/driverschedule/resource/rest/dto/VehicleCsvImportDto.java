package com.kernotec.driverschedule.resource.rest.dto;

import com.kernotec.driverschedule.resource.jpa.enums.VehicleTypeEnum;
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
