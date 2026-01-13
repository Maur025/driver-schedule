package com.kernotec.driverscheduleauth.rest.command.user.role;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.jpa.entity.Role;
import com.kernotec.driverscheduleauth.jpa.entity.UserRole;
import com.kernotec.driverscheduleauth.jpa.service.RoleService;
import com.kernotec.driverscheduleauth.jpa.service.UserRoleService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserRoleSaveAllByNameCmd extends
    AbstractTransactionalRequiredCommand<UserRoleSaveAllByNameCmd.Request, List<UserRole>>
{

    private final RoleService roleService;
    private final UserRoleService userRoleService;

    @Override
    protected List<UserRole> run(Request request) {
        List<Role> roleList = roleService.findAllByRealmIdAndResource(
            request.realmId, request.resource);

        if (roleList.isEmpty()) {
            log.warn(
                "No roles found for realmId '{}' and resource '{}'", request.realmId,
                request.resource
            );
            return null;
        }

        Map<String, Role> roleMapByName = roleList.stream()
            .collect(Collectors.toMap(Role::getName, role -> role));

        List<UserRole> userRoleListToSave = new ArrayList<>();

        for (String roleName : request.roleNames()) {
            if (!roleMapByName.containsKey(roleName)) {
                log.warn(
                    "Role with name '{}' not found in realmId '{}' and resource '{}'",
                    roleName, request.realmId, request.resource
                );
                continue;
            }

            Role role = roleMapByName.get(roleName);
            UserRole userRole = getUserRole(request.userId, role.getId());

            userRoleListToSave.add(userRole);
        }

        return userRoleService.saveAll(userRoleListToSave);
    }

    private UserRole getUserRole(UUID userId, UUID roleId) {
        var userRole = new UserRole();

        userRole.setUserId(userId);
        userRole.setRoleId(roleId);

        return userRole;
    }

    @Builder
    public record Request(@NotNull @NotEmpty Set<String> roleNames, @NotNull UUID realmId,
                          @NotNull String resource, @NotNull UUID userId)
    {

    }
}
