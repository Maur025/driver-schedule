package com.kernotec.driverschedule.service.webflux.user.spec.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class UserUpdateRequest extends BaseRequest {

    private String name;
    private String lastName;
    private String username;
    private Set<String> roles;
    private String resource;
}
