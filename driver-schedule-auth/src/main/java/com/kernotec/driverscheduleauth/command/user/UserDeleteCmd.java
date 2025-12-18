package com.kernotec.driverscheduleauth.command.user;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDeleteCmd extends
    AbstractTransactionalRequiredCommand<UserDeleteCmd.Request, Void>
{

    private final UserService userService;

    @Override
    protected Void run(Request request) {
        userService.deleteById(request.userId);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID userId) {

    }
}
