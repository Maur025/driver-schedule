package com.kernotec.driverscheduleauth.rest.dto.request.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@JsonInclude(Include.NON_NULL)
public class AccountResetPasswordRequest extends BaseRequest {

    @NotNull
    @NotBlank
    private String adminPassword;

    @NotNull
    private UUID userId;
    @NotNull
    @NotBlank
    private String newUserPassword;
}
