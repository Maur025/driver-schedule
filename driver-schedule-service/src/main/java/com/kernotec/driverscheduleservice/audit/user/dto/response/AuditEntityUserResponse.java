package com.kernotec.driverscheduleservice.audit.user.dto.response;

import com.kernotec.core.rest.dto.response.data.AuditEntityResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AuditEntityUserResponse extends AuditEntityResponse {

    private AuthUserDataResponse createdByUser;
    private AuthUserDataResponse updatedByUser;
}
