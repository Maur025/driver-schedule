package com.kernotec.driverscheduleservice.audit.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.AuditEntityResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class AuditEntityUserResponse extends AuditEntityResponse {

    private AuthUserDataResponse createdByUser;
    private AuthUserDataResponse updatedByUser;
}
