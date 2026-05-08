package com.kernotec.driverschedule.service.jpa.dto.trip;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripStateDto extends AuditEntityDto {

    private String name;
    private String code;
}
