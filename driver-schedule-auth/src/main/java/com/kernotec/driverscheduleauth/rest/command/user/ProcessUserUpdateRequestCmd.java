package com.kernotec.driverscheduleauth.rest.command.user;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.command.user.UserGetDtoCmd;
import com.kernotec.driverscheduleauth.command.user.UserUpdateCmd;
import com.kernotec.driverscheduleauth.exception.UserException;
import com.kernotec.driverscheduleauth.jpa.dto.UserDto;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.service.UserRoleService;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import com.kernotec.driverscheduleauth.rest.command.user.role.UserRoleSaveAllByNameCmd;
import com.kernotec.driverscheduleauth.rest.dto.request.user.UserUpdateRequest;
import com.kernotec.driverscheduleauth.util.UserUtil;
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
public class ProcessUserUpdateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessUserUpdateRequestCmd.Request, Void>
{

    private final UserService userService;
    private final UserRoleService userRoleService;

    private final UserUtil userUtil;

    private final UserUpdateCmd userUpdateCmd;
    private final UserGetDtoCmd userGetDtoCmd;
    private final UserRoleSaveAllByNameCmd userRoleSaveAllByNameCmd;

    @Override
    protected void validate(Request request) {
        UserUpdateRequest userUpdateRequest = request.userUpdateRequest;
        String usernameSanitized = userUtil.getUsernameSanitized(userUpdateRequest.getUsername());

        if (usernameSanitized == null) {
            log.debug("Username not changed, skipping validation");
            return;
        }

        Optional<User> userOptional = userService.findByUsernameAndIdNot(
            usernameSanitized, request.userId);

        if (userOptional.isPresent()) {
            throw new UserException(
                "already.exists", "'" + userUpdateRequest.getUsername() + "'",
                HttpStatus.CONFLICT.value()
            );
        }
    }

    @Override
    protected Void run(Request request) {
        UserUpdateRequest userUpdateRequest = request.userUpdateRequest;

        UserDto userDto = userGetDtoCmd.withRequest(UserGetDtoCmd.Request.builder()
                .userId(request.userId)
                .build())
            .execute();

        userUpdateCmd.withRequest(UserUpdateCmd.Request.builder()
                .userId(request.userId)
                .name(userUpdateRequest.getName())
                .lastName(userUpdateRequest.getLastName())
                .build())
            .execute();

        if (userUpdateRequest.getRoles() == null || userUpdateRequest.getRoles()
            .isEmpty())
        {
            log.debug("No roles to update for user id '{}'", request.userId);
            return null;
        }

        userRoleService.deleteAllByUserId(request.userId);

        userRoleSaveAllByNameCmd.withRequest(UserRoleSaveAllByNameCmd.Request.builder()
                .roleNames(userUpdateRequest.getRoles())
                .realmId(userDto.getRealmId())
                .resource(userUpdateRequest.getResource())
                .userId(request.userId)
                .build())
            .execute();

        return null;
    }

    @Builder
    public record Request(@NotNull UUID userId, @NotNull UserUpdateRequest userUpdateRequest) {

    }
}
