package com.kernotec.driverscheduleauth.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserMatchesPasswordCmd extends
    AbstractTransactionalRequiredCommand<UserMatchesPasswordCmd.Request, Boolean>
{

    private final PasswordEncoder passwordEncoder;

    @Override
    protected Boolean run(Request request) {
        return passwordEncoder.matches(request.password, request.hashedPassword);
    }

    @Builder
    public record Request(@NotNull String password, @NotNull String hashedPassword) {

    }
}
