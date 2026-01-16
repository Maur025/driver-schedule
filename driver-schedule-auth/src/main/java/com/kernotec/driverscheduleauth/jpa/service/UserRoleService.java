package com.kernotec.driverscheduleauth.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleauth.jpa.entity.UserRole;
import com.kernotec.driverscheduleauth.jpa.repository.UserRoleRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserRoleService extends BaseServiceImpl<UserRole, UUID> {

    private final UserRoleRepository repository;

    @Override
    protected String resourceName() {
        return "User Role";
    }

    @Override
    protected BaseRepository<UserRole, UUID> repository() {
        return repository;
    }

    public void deleteAllByUserId(UUID userId) {
        repository.deleteAllByUserId(userId);
    }
}
