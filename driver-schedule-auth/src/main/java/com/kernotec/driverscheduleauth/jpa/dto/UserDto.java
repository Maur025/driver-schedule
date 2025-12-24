package com.kernotec.driverscheduleauth.jpa.dto;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends AuditEntityDto {

    private String name;
    private String lastName;
    private String username;
    private String password;
    private ZonedDateTime createdOn;

    private UUID realmId;
    private RealmDto realm;

    private Set<RoleDto> roles;
}
