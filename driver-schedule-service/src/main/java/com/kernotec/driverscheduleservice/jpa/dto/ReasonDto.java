package com.kernotec.driverscheduleservice.jpa.dto;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReasonDto extends AuditEntityDto {

    private String reasonDescription;
}
