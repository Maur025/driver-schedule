package com.kernotec.driverscheduleauth.rest.dto.request.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class UserUpdateRequest extends BaseRequest {

    private String name;
    private String lastName;
    private String username;
    private Set<String> roles;
    private String resource;
}
