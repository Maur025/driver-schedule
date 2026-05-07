package com.kernotec.driverschedule.service.audit.user.mapper;

import com.kernotec.driverschedule.service.audit.user.dto.AuthUserDataDto;
import com.kernotec.driverschedule.service.audit.user.json.AuditUserData;
import com.kernotec.driverschedule.service.audit.user.json.AuthUserData;
import org.mapstruct.Mapper;

@Mapper
public interface AuthUserDataDtoMapper {

    default AuthUserDataDto toResponse(AuditUserData auditUserData) {
        if (auditUserData == null) {
            return null;
        }

        if (auditUserData instanceof AuthUserData authUserData) {
            var dto = new AuthUserDataDto();

            dto.setId(authUserData.getId());
            dto.setUsername(authUserData.getUsername());
            dto.setName(authUserData.getName());
            dto.setRoles(authUserData.getRoles());
            dto.setAuthTime(authUserData.getAuthTime());

            return dto;
        }

        return null;
    }
}
