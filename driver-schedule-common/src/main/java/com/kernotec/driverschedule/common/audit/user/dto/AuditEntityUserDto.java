package com.kernotec.driverschedule.common.audit.user.dto;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AuditEntityUserDto extends AuditEntityDto {

    private AuthUserDataDto createdByUser;
    private AuthUserDataDto updatedByUser;
}
