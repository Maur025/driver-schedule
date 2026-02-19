package com.kernotec.driverscheduleauth.rest.command.user;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.command.user.UserDeleteCmd;
import com.kernotec.driverscheduleauth.exception.UserException;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import com.kernotec.driverscheduleauth.rest.dto.request.user.UserDeleteRequest;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessUserDeleteRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessUserDeleteRequestCmd.Request, Void>
{

    private final UserService userService;
    private final UserDeleteCmd userDeleteCmd;

    @Override
    protected void validate(Request request) {
        UserDeleteRequest userDeleteRequest = request.userDeleteRequest;

        User user = userService.findByIdThrow(request.userId);

        Optional<User> userOptional = userService.findByUsername(userDeleteRequest.getUserName());

        if (userOptional.isEmpty()) {
            throw new UserException(
                "username.not.found",
                "'" + userDeleteRequest.getUserName() + "'", HttpStatus.BAD_REQUEST.value()
            );
        }

        if (!request.realmId.equals(user.getRealmId())) {
            throw new UserException(
                "realm.verification.failed", "", HttpStatus.BAD_REQUEST.value());
        }
    }

    @Override
    protected Void run(Request request) {
        userDeleteCmd.withRequest(UserDeleteCmd.Request.builder()
                .userId(request.userId)
                .build())
            .execute();

        return null;
    }

    @Builder
    public record Request(@NotNull UUID userId, @NotNull UserDeleteRequest userDeleteRequest,
                          @NotNull UUID realmId)
    {

    }
}
