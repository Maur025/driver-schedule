package com.kernotec.driverscheduleauth.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.driverscheduleauth.jpa.entity.Role;
import com.kernotec.driverscheduleauth.jpa.repository.RoleRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RoleService extends BaseServiceImpl<Role, UUID> {

    private final RoleRepository repository;

    @Override
    protected String resourceName() {
        return "Role";
    }

    @Override
    protected BaseRepository<Role, UUID> repository() {
        return repository;
    }


    public Optional<Role> findByNameAndRealmIdAndResource(String name, UUID realmId,
        String resource)
    {
        return repository.findByNameAndRealmIdAndResource(name, realmId, resource);
    }
}
