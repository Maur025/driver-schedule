package com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class UserDeleteRequest extends BaseRequest {

    @NotNull
    @NotBlank
    private String realmName;

    @NotNull
    @NotBlank
    private String userName;
}
