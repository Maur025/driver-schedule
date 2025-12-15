package com.kernotec.driverscheduleauth.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserCreateCmd extends
    AbstractTransactionalRequiredCommand<UserCreateCmd.Request, UUID>
{

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected UUID run(Request request) {
        var user = new User();

        user.setName(request.name);
        user.setLastName(request.lastName);
        user.setUsername(request.username);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setCreatedOn(ZonedDateTime.now());
        user.setRealmId(request.realmId);

        user = userService.save(user);
        return user.getId();
    }

    @Builder
    public record Request(@NotNull @NotBlank String name, @NotNull String lastName,
                          @NotNull @NotBlank String username, @NotNull @NotBlank String password,
                          @NotNull UUID realmId)
    {

    }
}
