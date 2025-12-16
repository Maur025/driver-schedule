package com.kernotec.driverscheduleauth.jpa.dto;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealmDto extends AuditEntityDto {

    private String name;
}
