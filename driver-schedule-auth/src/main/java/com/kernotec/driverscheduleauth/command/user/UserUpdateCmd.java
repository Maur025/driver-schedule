package com.kernotec.driverscheduleauth.command.user;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserUpdateCmd extends
    AbstractTransactionalRequiredCommand<UserUpdateCmd.Request, UUID>
{

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected UUID run(Request request) {
        var user = userService.findByIdThrow(request.userId);

        if (request.name != null && !request.name.isBlank()) {
            user.setName(request.name);
        }
        if (request.lastName != null) {
            user.setLastName(request.lastName);
        }
        if (request.password != null && !request.password.isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password));
        }

        user = userService.save(user);
        return user.getId();
    }

    @Builder
    public record Request(@NotNull UUID userId, String name, String lastName, String password) {

    }
}
