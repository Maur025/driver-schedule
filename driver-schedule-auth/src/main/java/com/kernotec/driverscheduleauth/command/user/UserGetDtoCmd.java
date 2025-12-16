package com.kernotec.driverscheduleauth.command.user;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleauth.jpa.dto.UserDto;
import com.kernotec.driverscheduleauth.jpa.dto.mapper.UserDtoMapper;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.service.UserService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserGetDtoCmd extends
    AbstractTransactionalRequiredCommand<UserGetDtoCmd.Request, UserDto>
{

    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    @Override
    protected UserDto run(Request request) {
        User user = userService.findByIdThrow(request.userId);
        return userDtoMapper.toDto(user);
    }

    @Builder
    public record Request(@NotNull UUID userId) {

    }
}
