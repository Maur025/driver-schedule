package com.kernotec.driverscheduleauth.rest.command.account;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.command.user.UserGetDtoCmd;
import com.kernotec.driverscheduleauth.command.user.UserUpdateCmd;
import com.kernotec.driverscheduleauth.exception.UserException;
import com.kernotec.driverscheduleauth.jpa.dto.UserDto;
import com.kernotec.driverscheduleauth.rest.dto.request.account.AccountChangePasswordRequest;
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
public class ProcessAccountChangePasswordCmd extends
    AbstractTransactionalRequiredCommand<ProcessAccountChangePasswordCmd.Request, Void>
{

    private final PasswordEncoder passwordEncoder;
    private final UserGetDtoCmd userGetDtoCmd;
    private final UserUpdateCmd userUpdateCmd;

    @Override
    protected void validate(Request request) {
        AccountChangePasswordRequest accountChangePasswordRequest = request.accountChangePasswordRequest;

        UserDto userDto = userGetDtoCmd.withRequest(UserGetDtoCmd.Request.builder()
                .userId(request.userId)
                .build())
            .execute();

        if (!passwordEncoder.matches(
            accountChangePasswordRequest.getCurrentPassword(), userDto.getPassword()))
        {
            throw new UserException(
                "current.password.not.match", "", HttpStatus.BAD_REQUEST.value());
        }

        if (!accountChangePasswordRequest.getNewPassword()
            .equals(accountChangePasswordRequest.getConfirmNewPassword()))
        {
            throw new UserException(
                "new.password.confirmation.not.match", "", HttpStatus.BAD_REQUEST.value());
        }
    }

    @Override
    protected Void run(Request request) {
        AccountChangePasswordRequest accountChangePasswordRequest = request.accountChangePasswordRequest;

        userUpdateCmd.withRequest(UserUpdateCmd.Request.builder()
                .userId(request.userId)
                .password(accountChangePasswordRequest.getNewPassword())
                .build())
            .execute();

        return null;
    }

    @Builder
    public record Request(@NotNull @Valid AccountChangePasswordRequest accountChangePasswordRequest,
                          @NotNull UUID userId)
    {

    }
}
