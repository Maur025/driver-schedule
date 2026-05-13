package com.kernotec.driverschedule.resource.jpa.dto;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleTypeDto extends AuditEntityDto {

    private String name;
    private String code;
}
