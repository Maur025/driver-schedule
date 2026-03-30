package com.kernotec.driverscheduleservice.audit.user.mapper;

import com.kernotec.driverscheduleservice.audit.user.dto.response.AuthUserDataResponse;
import com.kernotec.driverscheduleservice.audit.user.json.AuditUserData;
import com.kernotec.driverscheduleservice.audit.user.json.AuthUserData;
import org.mapstruct.Mapper;

@Mapper
public interface AuthUserDataResponseMapper {

    default AuthUserDataResponse toResponse(AuditUserData auditUserData) {
        if (auditUserData == null) {
            return null;
        }

        if (auditUserData instanceof AuthUserData authUserData) {
            var response = new AuthUserDataResponse();

            response.setId(authUserData.getId());
            response.setUsername(authUserData.getUsername());
            response.setName(authUserData.getName());
            // response.setRoles(authUserData.getRoles());
            response.setAuthTime(authUserData.getAuthTime());

            return response;
        }

        return null;
    }
}
