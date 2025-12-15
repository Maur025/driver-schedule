package com.kernotec.driverscheduleauth.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleauth.jpa.entity.User;
import com.kernotec.driverscheduleauth.jpa.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService extends BaseServiceImpl<User, UUID> {

    private final UserRepository repository;

    @Override
    protected String resourceName() {
        return "User";
    }

    @Override
    protected BaseRepository<User, UUID> repository() {
        return repository;
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User findByUsernameThrow(String username){
        return  findByUsername(username).orElseThrow(()-> new )
    }
}
