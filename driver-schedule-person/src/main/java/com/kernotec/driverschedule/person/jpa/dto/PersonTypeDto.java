package com.kernotec.driverschedule.person.jpa.dto;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonTypeDto extends AuditEntityDto {

    private String name;
    private String code;
}
