package com.kernotec.driverscheduleauth.rest.command.user;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.command.user.UserCreateCmd;
import com.kernotec.driverscheduleauth.exception.UserException;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import com.kernotec.driverscheduleauth.rest.command.user.role.UserRoleSaveAllByNameCmd;
import com.kernotec.driverscheduleauth.rest.dto.request.user.UserCreateRequest;
import com.kernotec.driverscheduleauth.util.UserUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessUserCreateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessUserCreateRequestCmd.Request, UUID>
{

    private final UserService userService;

    private final UserCreateCmd userCreateCmd;
    private final UserUtil userUtil;
    private final UserRoleSaveAllByNameCmd userRoleSaveAllByNameCmd;

    @Override
    protected void validate(Request request) {
        UserCreateRequest userCreateRequest = request.userCreateRequest;

        String usernameSanitized = userUtil.getUsernameSanitized(userCreateRequest.getUsername());
        Optional<User> userOptional = userService.findByUsername(usernameSanitized);

        if (userOptional.isPresent()) {
            throw new UserException(
                "already.exists", "'" + userCreateRequest.getUsername() + "'",
                HttpStatus.CONFLICT.value()
            );
        }
    }

    @Override
    protected UUID run(Request request) {
        UserCreateRequest userCreateRequest = request.userCreateRequest;

        String usernameSanitized = userUtil.getUsernameSanitized(userCreateRequest.getUsername());

        UUID userId = userCreateCmd.withRequest(UserCreateCmd.Request.builder()
                .name(userCreateRequest.getName())
                .lastName(userCreateRequest.getLastName())
                .username(usernameSanitized)
                .password(userCreateRequest.getPassword())
                .realmId(request.realmId)
                .build())
            .execute();

        if (userCreateRequest.getRoles()
            .isEmpty())
        {
            log.warn("No roles assigned to user '{}'", userCreateRequest.getUsername());
            return userId;
        }

        userRoleSaveAllByNameCmd.withRequest(UserRoleSaveAllByNameCmd.Request.builder()
                .roleNames(userCreateRequest.getRoles())
                .realmId(request.realmId)
                .resource(userCreateRequest.getResource())
                .userId(userId)
                .build())
            .execute();

        return userId;
    }

    @Builder
    public record Request(@NotNull @Valid UserCreateRequest userCreateRequest,
                          @NotNull UUID realmId)
    {

    }
}
