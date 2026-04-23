package com.kernotec.driverscheduleservice.rest.dto.resource;

import com.kernotec.driverscheduleservice.jpa.enums.resource.VehicleTypeEnum;
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
