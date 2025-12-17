package com.kernotec.driverscheduleauth.rest.dto.request.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class UserCreateRequest extends BaseRequest {

    @NotNull
    private String name;
    @NotNull
    private String lastName;
    @NotNull
    private String username;
    @NotNull
    private String password;

    @NotNull
    @NotBlank
    private String realmName;

    @NotNull
    private Set<String> roles;

    @NotNull
    @NotBlank
    private String resource;
}
