package com.kernotec.driverschedule.service.trip.jpa.dto;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripStateDto extends AuditEntityDto {

    private String name;
    private String code;
}
