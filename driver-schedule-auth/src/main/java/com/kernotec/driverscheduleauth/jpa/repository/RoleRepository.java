package com.kernotec.driverscheduleauth.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.driverscheduleauth.jpa.entity.Role;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BaseRepository<Role, UUID> {

    Optional<Role> findByNameAndRealmIdAndResource(String name, UUID realmId, String resource);

    List<Role> findAllByRealmIdAndResource(UUID realmId, String resource);
}
