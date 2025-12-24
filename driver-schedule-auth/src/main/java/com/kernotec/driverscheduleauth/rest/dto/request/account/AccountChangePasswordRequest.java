package com.kernotec.driverscheduleauth.rest.dto.request.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class AccountChangePasswordRequest extends BaseRequest {

    @NotNull
    @NotBlank
    private String currentPassword;

    @NotNull
    @NotBlank
    private String newPassword;
    @NotNull
    @NotBlank
    private String confirmNewPassword;
}
