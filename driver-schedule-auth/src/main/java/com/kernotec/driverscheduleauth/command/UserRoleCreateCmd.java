package com.kernotec.driverscheduleauth.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.jpa.entity.UserRole;
import com.kernotec.driverscheduleauth.jpa.service.UserRoleService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserRoleCreateCmd extends
    AbstractTransactionalRequiredCommand<UserRoleCreateCmd.Request, UUID>
{

    private final UserRoleService userRoleService;

    @Override
    protected UUID run(Request request) {
        var userRole = new UserRole();

        userRole.setUserId(request.userId);
        userRole.setRoleId(request.roleId);

        userRole = userRoleService.save(userRole);
        return userRole.getId();
    }

    @Builder
    public record Request(@NotNull UUID userId, @NotNull UUID roleId) {

    }
}
