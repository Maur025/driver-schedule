package com.kernotec.driverscheduleauth.jpa.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.jpa.dto.AuditEntityDto;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class RoleDto extends AuditEntityDto {

    private String name;
    private String resource;

    private UUID realmId;
    private RealmDto realm;
}
