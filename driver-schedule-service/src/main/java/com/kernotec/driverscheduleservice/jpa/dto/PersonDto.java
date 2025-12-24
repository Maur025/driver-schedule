package com.kernotec.driverscheduleservice.jpa.dto;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDto extends AuditEntityDto {

    private String name;
    private String lastName;
    private String document;
    private String phone;
    private UUID userId;

    private Set<PersonTypeDto> personTypes;
}
