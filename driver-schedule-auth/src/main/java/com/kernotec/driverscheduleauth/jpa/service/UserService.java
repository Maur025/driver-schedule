package com.kernotec.driverscheduleauth.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleauth.exception.UserException;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.repository.UserRepository;
import com.kernotec.driverscheduleauth.util.UserUtil;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService extends BaseServiceImpl<User, UUID> {

    private final UserRepository repository;
    private final UserUtil userUtil;

    @Override
    protected String resourceName() {
        return "User";
    }

    @Override
    protected BaseRepository<User, UUID> repository() {
        return repository;
    }

    public Optional<User> findByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }

        String usernameSanitized = userUtil.getUsernameSanitized(username);

        return repository.findByUsernameIgnoreCase(usernameSanitized);
    }

    public Optional<User> findByUsernameAndIdNot(String username, UUID id) {
        if (username == null) {
            return Optional.empty();
        }

        String usernameSanitized = userUtil.getUsernameSanitized(username);

        return repository.findByUsernameIgnoreCaseAndIdNot(usernameSanitized, id);
    }

    public User findByUsernameThrow(String username) {
        return findByUsername(username).orElseThrow(
            () -> new UserException("login.failed", "", HttpStatus.BAD_REQUEST.value()));
    }
}
