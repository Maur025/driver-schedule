package com.kernotec.driverscheduleauth.rest.command.account;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.command.user.UserGetDtoCmd;
import com.kernotec.driverscheduleauth.command.user.UserUpdateCmd;
import com.kernotec.driverscheduleauth.exception.UserException;
import com.kernotec.driverscheduleauth.jpa.dto.UserDto;
import com.kernotec.driverscheduleauth.rest.dto.request.account.AccountResetPasswordRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessAccountResetPasswordCmd extends
    AbstractTransactionalRequiredCommand<ProcessAccountResetPasswordCmd.Request, Void>
{

    private final UserGetDtoCmd userGetDtoCmd;
    private final PasswordEncoder passwordEncoder;
    private final UserUpdateCmd userUpdateCmd;

    @Override
    protected void validate(Request request) {
        AccountResetPasswordRequest accountResetPasswordRequest = request.accountResetPasswordRequest;

        userGetDtoCmd.withRequest(UserGetDtoCmd.Request.builder()
                .userId(accountResetPasswordRequest.getUserId())
                .build())
            .execute();

        UserDto userAdminDto = userGetDtoCmd.withRequest(UserGetDtoCmd.Request.builder()
                .userId(request.adminUserId)
                .build())
            .execute();

        if (!passwordEncoder.matches(
            accountResetPasswordRequest.getAdminPassword(), userAdminDto.getPassword()))
        {
            throw new UserException("current.password.not.match", "", HttpStatus.BAD_REQUEST.value());
        }
    }

    @Override
    protected Void run(Request request) {
        AccountResetPasswordRequest accountResetPasswordRequest = request.accountResetPasswordRequest;

        userUpdateCmd.withRequest(UserUpdateCmd.Request.builder()
                .userId(accountResetPasswordRequest.getUserId())
                .password(accountResetPasswordRequest.getNewUserPassword())
                .build())
            .execute();

        return null;
    }

    @Builder
    public record Request(@NotNull @Valid AccountResetPasswordRequest accountResetPasswordRequest,
                          @NotNull UUID adminUserId)
    {

    }
}
