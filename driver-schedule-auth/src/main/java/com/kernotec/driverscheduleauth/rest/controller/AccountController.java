package com.kernotec.driverscheduleauth.rest.controller;

import com.kernotec.core.rest.dto.response.MessageResponse;
import com.kernotec.driverscheduleauth.rest.ApiSpec.AccountSpec;
import com.kernotec.driverscheduleauth.rest.command.account.ProcessAccountChangePasswordCmd;
import com.kernotec.driverscheduleauth.rest.command.account.ProcessAccountResetPasswordCmd;
import com.kernotec.driverscheduleauth.rest.dto.request.account.AccountChangePasswordRequest;
import com.kernotec.driverscheduleauth.rest.dto.request.account.AccountResetPasswordRequest;
import com.kernotec.driverscheduleauth.util.AuthUtil;
import com.kernotec.driverscheduleauth.util.RoleUtil.IsRoleAdmin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = AccountSpec.TAG_NAME, description = AccountSpec.TAG_DESCRIPTION)
@RequestMapping(path = AccountSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class AccountController {

    private final AuthUtil authUtil;
    private final ProcessAccountChangePasswordCmd processAccountChangePasswordCmd;
    private final ProcessAccountResetPasswordCmd processAccountResetPasswordCmd;

    @Operation(summary = "account change password")
    @PostMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse changePassword(@RequestBody AccountChangePasswordRequest request,
        Authentication authentication)
    {
        processAccountChangePasswordCmd.withRequest(
                ProcessAccountChangePasswordCmd.Request.builder()
                    .accountChangePasswordRequest(request)
                    .userId(authUtil.getUserIdFromAuthentication(authentication))
                    .build())
            .execute();

        return MessageResponse.builder()
            .code(HttpStatus.OK.value())
            .message("Password changed successfully")
            .build();
    }

    @Operation(summary = "account reset password")
    @PostMapping("/password/reset")
    @ResponseStatus(HttpStatus.OK)
    @IsRoleAdmin
    public MessageResponse resetPassword(@RequestBody AccountResetPasswordRequest request,
        Authentication authentication)
    {
        processAccountResetPasswordCmd.withRequest(ProcessAccountResetPasswordCmd.Request.builder()
                .accountResetPasswordRequest(request)
                .adminUserId(authUtil.getUserIdFromAuthentication(authentication))
                .build())
            .execute();

        return MessageResponse.builder()
            .code(HttpStatus.OK.value())
            .message("Password reset successfully")
            .build();
    }
}
