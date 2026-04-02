package com.kernotec.driverscheduleauth.rest.controller;

import com.kernotec.core.rest.dto.response.MessageResponse;
import com.kernotec.driverscheduleauth.common.annotation.CanUserCredential;
import com.kernotec.driverscheduleauth.common.annotation.CanUserResetPassword;
import com.kernotec.driverscheduleauth.jpa.service.RealmService;
import com.kernotec.driverscheduleauth.rest.ApiSpec.AccountSpec;
import com.kernotec.driverscheduleauth.rest.command.account.ProcessAccountChangePasswordCmd;
import com.kernotec.driverscheduleauth.rest.command.account.ProcessAccountResetPasswordCmd;
import com.kernotec.driverscheduleauth.rest.dto.request.account.AccountChangePasswordRequest;
import com.kernotec.driverscheduleauth.rest.dto.request.account.AccountResetPasswordRequest;
import com.kernotec.driverscheduleauth.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final RealmService realmService;

    @Operation(summary = "account change password")
    @PostMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    @CanUserCredential
    public MessageResponse changePassword(@PathVariable String realm,
        @RequestBody AccountChangePasswordRequest request, Authentication authentication)
    {
        realmService.findRealmIdByNameInCache(realm);

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
    @CanUserResetPassword
    public MessageResponse resetPassword(@PathVariable String realm,
        @RequestBody AccountResetPasswordRequest request, Authentication authentication)
    {
        realmService.findRealmIdByNameInCache(realm);

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
