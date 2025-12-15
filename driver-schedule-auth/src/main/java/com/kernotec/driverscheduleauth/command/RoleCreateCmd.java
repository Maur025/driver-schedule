package com.kernotec.driverscheduleauth.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.jpa.entity.Role;
import com.kernotec.driverscheduleauth.jpa.service.RoleService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleCreateCmd extends
    AbstractTransactionalRequiredCommand<RoleCreateCmd.Request, UUID>
{

    private final RoleService roleService;

    @Override
    protected UUID run(Request request) {
        var role = new Role();

        role.setName(request.name);
        role.setResource(request.resource);
        role.setRealmId(request.realmId);

        role = roleService.save(role);
        return role.getId();
    }

    @Builder
    public record Request(@NotNull String name, @NotNull String resource, @NotNull UUID realmId) {

    }
}
