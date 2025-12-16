package com.kernotec.driverscheduleauth.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.driverscheduleauth.jpa.dto.RoleDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class OpenIdConnectUserInfoResponse {

    private String sub;
    private String username;
    private String name;
    private List<RoleDto> roles;
}
